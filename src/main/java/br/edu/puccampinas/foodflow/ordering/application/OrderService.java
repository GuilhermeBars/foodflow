package br.edu.puccampinas.foodflow.ordering.application;

import br.edu.puccampinas.foodflow.catalog.application.port.in.CatalogQuery;
import br.edu.puccampinas.foodflow.catalog.domain.MenuItem;
import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import br.edu.puccampinas.foodflow.ordering.application.port.in.AdvanceOrderUseCase;
import br.edu.puccampinas.foodflow.ordering.application.port.in.OrderQuery;
import br.edu.puccampinas.foodflow.ordering.application.port.in.OrderTransition;
import br.edu.puccampinas.foodflow.ordering.application.port.in.PlaceOrderUseCase;
import br.edu.puccampinas.foodflow.ordering.application.port.out.OrderRepository;
import br.edu.puccampinas.foodflow.ordering.domain.Order;
import br.edu.puccampinas.foodflow.ordering.domain.OrderItem;
import br.edu.puccampinas.foodflow.payment.application.port.in.ProcessPaymentUseCase;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import br.edu.puccampinas.foodflow.pricing.domain.CouponPercentageDiscount;
import br.edu.puccampinas.foodflow.pricing.domain.DeliveryFeeStrategy;
import br.edu.puccampinas.foodflow.pricing.domain.DiscountStrategy;
import br.edu.puccampinas.foodflow.pricing.domain.DistanceBasedDeliveryFee;
import br.edu.puccampinas.foodflow.pricing.domain.FreeAboveThresholdDeliveryFee;
import br.edu.puccampinas.foodflow.pricing.domain.NoDiscount;
import br.edu.puccampinas.foodflow.pricing.domain.OrderPricing;
import br.edu.puccampinas.foodflow.pricing.domain.PriceQuote;
import br.edu.puccampinas.foodflow.shared.application.DomainEventPublisher;
import br.edu.puccampinas.foodflow.shared.domain.BusinessRuleException;
import br.edu.puccampinas.foodflow.shared.domain.EntityNotFoundException;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orquestra os casos de uso de pedido. Coordena catalogo, precificacao,
 * pagamento, persistencia e publicacao de eventos, dependendo apenas das portas
 * de cada modulo (DIP) e mantendo as regras de transicao no proprio agregado.
 */
@Service
public class OrderService implements PlaceOrderUseCase, AdvanceOrderUseCase, OrderQuery {

    private static final Money FREE_DELIVERY_THRESHOLD = Money.brl("80.00");

    private final CatalogQuery catalog;
    private final ProcessPaymentUseCase payment;
    private final OrderRepository orders;
    private final DomainEventPublisher events;
    private final OrderPricing pricing = new OrderPricing();

    public OrderService(CatalogQuery catalog,
                        ProcessPaymentUseCase payment,
                        OrderRepository orders,
                        DomainEventPublisher events) {
        this.catalog = catalog;
        this.payment = payment;
        this.orders = orders;
        this.events = events;
    }

    @Override
    @Transactional
    public Order place(PlaceOrderCommand command) {
        Restaurant restaurant = catalog.getRestaurant(command.restaurantId());
        List<OrderItem> items = command.items().stream()
                .map(line -> toOrderItem(restaurant, line))
                .toList();

        Order order = Order.place(restaurant.id(), command.customerId(), items);
        PriceQuote quote = pricing.quote(
                order.subtotal(), command.distanceKm(), deliveryFeeStrategy(), discountStrategy(command.coupon()));

        chargeOrThrow(order.id(), quote, command);

        orders.save(order);
        events.publishEventsOf(order);
        return order;
    }

    @Override
    @Transactional
    public Order apply(UUID orderId, OrderTransition transition) {
        Order order = loadOrder(orderId);
        switch (transition) {
            case CONFIRM -> order.confirm();
            case START_PREPARING -> order.startPreparing();
            case DISPATCH -> order.dispatch();
            case DELIVER -> order.deliver();
            case CANCEL -> order.cancel();
        }
        orders.save(order);
        events.publishEventsOf(order);
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(UUID id) {
        return loadOrder(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> listOrders(Pageable pageable) {
        return orders.findAll(pageable);
    }

    private OrderItem toOrderItem(Restaurant restaurant, PlaceOrderCommand.Line line) {
        MenuItem item = restaurant.requireItem(line.menuItemId());
        return new OrderItem(item.id(), item.name(), item.price(), line.quantity());
    }

    private void chargeOrThrow(UUID orderId, PriceQuote quote, PlaceOrderCommand command) {
        PaymentResult result = payment.pay(new PaymentRequest(
                orderId, quote.total(), command.paymentMethod(), orderId.toString()));
        if (!result.isApproved()) {
            throw new BusinessRuleException("Pagamento recusado: " + result.message());
        }
    }

    private DeliveryFeeStrategy deliveryFeeStrategy() {
        return new FreeAboveThresholdDeliveryFee(
                FREE_DELIVERY_THRESHOLD, new DistanceBasedDeliveryFee(Money.brl("5.00"), Money.brl("1.20")));
    }

    private DiscountStrategy discountStrategy(String coupon) {
        if (coupon != null && !coupon.isBlank()) {
            return new CouponPercentageDiscount(BigDecimal.valueOf(10));
        }
        return new NoDiscount();
    }

    private Order loadOrder(UUID id) {
        return orders.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido %s nao encontrado".formatted(id)));
    }
}
