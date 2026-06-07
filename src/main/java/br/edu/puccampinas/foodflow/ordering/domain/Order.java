package br.edu.puccampinas.foodflow.ordering.domain;

import br.edu.puccampinas.foodflow.shared.domain.AggregateRoot;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Raiz do agregado de pedidos. Toda a regra de transicao vive nos objetos
 * {@link OrderState}; o pedido apenas delega a operacao ao estado atual, o que
 * mantem este agregado pequeno e aberto a novos estados sem modificacao (OCP).
 */
public class Order extends AggregateRoot {

    private final UUID id;
    private final UUID restaurantId;
    private final String customerId;
    private final List<OrderItem> items;
    private OrderState state;

    private Order(UUID id, UUID restaurantId, String customerId, List<OrderItem> items, OrderState state) {
        this.id = Objects.requireNonNull(id, "id");
        this.restaurantId = Objects.requireNonNull(restaurantId, "restaurantId");
        this.state = Objects.requireNonNull(state, "state");
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Cliente do pedido e obrigatorio");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Pedido precisa de ao menos um item");
        }
        this.customerId = customerId.trim();
        this.items = List.copyOf(items);
    }

    public static Order place(UUID restaurantId, String customerId, List<OrderItem> items) {
        Order order = new Order(UUID.randomUUID(), restaurantId, customerId, items, CreatedState.INSTANCE);
        order.registerEvent(new OrderStatusChangedEvent(order.id, null, OrderStatus.CREATED, Instant.now()));
        return order;
    }

    public static Order reconstitute(UUID id, UUID restaurantId, String customerId,
                                     List<OrderItem> items, OrderStatus status) {
        return new Order(id, restaurantId, customerId, items, OrderState.forStatus(status));
    }

    public void confirm() {
        state.confirm(this);
    }

    public void startPreparing() {
        state.startPreparing(this);
    }

    public void dispatch() {
        state.dispatch(this);
    }

    public void deliver() {
        state.deliver(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    /** Aplica a transicao e publica o evento. Visivel apenas para os estados. */
    void transitionTo(OrderState target) {
        OrderStatus previous = state.status();
        this.state = target;
        registerEvent(new OrderStatusChangedEvent(id, previous, target.status(), Instant.now()));
    }

    public Money subtotal() {
        return items.stream().map(OrderItem::subtotal).reduce(Money.zero(), Money::add);
    }

    public OrderStatus status() {
        return state.status();
    }

    public boolean isFinished() {
        return state.isTerminal();
    }

    public UUID id() {
        return id;
    }

    public UUID restaurantId() {
        return restaurantId;
    }

    public String customerId() {
        return customerId;
    }

    public List<OrderItem> items() {
        return items;
    }
}
