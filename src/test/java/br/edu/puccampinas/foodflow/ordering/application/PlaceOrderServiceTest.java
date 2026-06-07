package br.edu.puccampinas.foodflow.ordering.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.edu.puccampinas.foodflow.catalog.application.port.in.CatalogQuery;
import br.edu.puccampinas.foodflow.catalog.domain.MenuItem;
import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import br.edu.puccampinas.foodflow.ordering.application.port.in.PlaceOrderUseCase.PlaceOrderCommand;
import br.edu.puccampinas.foodflow.ordering.application.port.in.PlaceOrderUseCase.PlaceOrderCommand.Line;
import br.edu.puccampinas.foodflow.ordering.application.port.out.OrderRepository;
import br.edu.puccampinas.foodflow.ordering.domain.Order;
import br.edu.puccampinas.foodflow.ordering.domain.OrderStatus;
import br.edu.puccampinas.foodflow.payment.application.port.in.ProcessPaymentUseCase;
import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import br.edu.puccampinas.foodflow.payment.domain.PaymentStatus;
import br.edu.puccampinas.foodflow.shared.application.DomainEventPublisher;
import br.edu.puccampinas.foodflow.shared.domain.BusinessRuleException;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class PlaceOrderServiceTest {

    private final Restaurant restaurant = Restaurant.create("Cantina da Nonna", "Italiana");
    private final MenuItem pizza = restaurant.addMenuItem("Pizza", "Margherita", Money.brl("50.00"));

    private final CatalogQuery catalog = new CatalogQuery() {
        @Override
        public Restaurant getRestaurant(UUID id) {
            return restaurant;
        }

        @Override
        public List<Restaurant> listRestaurants() {
            return List.of(restaurant);
        }
    };

    private final InMemoryOrders orders = new InMemoryOrders();
    private final DomainEventPublisher events = new DomainEventPublisher(event -> { });

    private OrderService serviceWith(ProcessPaymentUseCase payment) {
        return new OrderService(catalog, payment, orders, events);
    }

    @Test
    void registraPedidoComItensDoCardapioEStatusInicial() {
        OrderService service = serviceWith(approvingPayment());
        PlaceOrderCommand command = new PlaceOrderCommand(
                restaurant.id(), "cliente-1", List.of(new Line(pizza.id(), 2)), PaymentMethod.PIX, 3.0, null);

        Order order = service.place(command);

        assertEquals(OrderStatus.CREATED, order.status());
        assertEquals(Money.brl("100.00"), order.subtotal());
        assertEquals(1, order.items().size());
        assertTrue(orders.findById(order.id()).isPresent());
    }

    @Test
    void recusaPedidoQuandoOPagamentoNaoEAprovado() {
        OrderService service = serviceWith(decliningPayment());
        PlaceOrderCommand command = new PlaceOrderCommand(
                restaurant.id(), "cliente-1", List.of(new Line(pizza.id(), 1)), PaymentMethod.CREDIT_CARD, 1.0, null);

        assertThrows(BusinessRuleException.class, () -> service.place(command));
    }

    private ProcessPaymentUseCase approvingPayment() {
        return request -> new PaymentResult("TX-1", PaymentStatus.APPROVED, "ok");
    }

    private ProcessPaymentUseCase decliningPayment() {
        return request -> new PaymentResult("TX-2", PaymentStatus.DECLINED, "saldo insuficiente");
    }

    private static final class InMemoryOrders implements OrderRepository {
        private final Map<UUID, Order> store = new HashMap<>();

        @Override
        public Order save(Order order) {
            store.put(order.id(), order);
            return order;
        }

        @Override
        public Optional<Order> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public Page<Order> findAll(Pageable pageable) {
            return new PageImpl<>(new ArrayList<>(store.values()), pageable, store.size());
        }
    }
}
