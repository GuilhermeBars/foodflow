package br.edu.puccampinas.foodflow.ordering.domain;

/** Pedido em preparo: pode ser despachado para entrega ou cancelado. */
final class PreparingState implements OrderState {

    static final PreparingState INSTANCE = new PreparingState();

    private PreparingState() {
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.PREPARING;
    }

    @Override
    public void dispatch(Order order) {
        order.transitionTo(OutForDeliveryState.INSTANCE);
    }

    @Override
    public void cancel(Order order) {
        order.transitionTo(CancelledState.INSTANCE);
    }
}
