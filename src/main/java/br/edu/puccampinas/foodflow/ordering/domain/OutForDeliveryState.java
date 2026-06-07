package br.edu.puccampinas.foodflow.ordering.domain;

/** Pedido saiu para entrega: so resta ser entregue. */
final class OutForDeliveryState implements OrderState {

    static final OutForDeliveryState INSTANCE = new OutForDeliveryState();

    private OutForDeliveryState() {
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.OUT_FOR_DELIVERY;
    }

    @Override
    public void deliver(Order order) {
        order.transitionTo(DeliveredState.INSTANCE);
    }
}
