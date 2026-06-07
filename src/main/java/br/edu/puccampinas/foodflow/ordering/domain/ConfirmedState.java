package br.edu.puccampinas.foodflow.ordering.domain;

/** Pedido confirmado pelo restaurante: pode entrar em preparo ou ser cancelado. */
final class ConfirmedState implements OrderState {

    static final ConfirmedState INSTANCE = new ConfirmedState();

    private ConfirmedState() {
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.CONFIRMED;
    }

    @Override
    public void startPreparing(Order order) {
        order.transitionTo(PreparingState.INSTANCE);
    }

    @Override
    public void cancel(Order order) {
        order.transitionTo(CancelledState.INSTANCE);
    }
}
