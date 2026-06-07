package br.edu.puccampinas.foodflow.ordering.domain;

/** Pedido recem-criado: pode ser confirmado ou cancelado. */
final class CreatedState implements OrderState {

    static final CreatedState INSTANCE = new CreatedState();

    private CreatedState() {
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.CREATED;
    }

    @Override
    public void confirm(Order order) {
        order.transitionTo(ConfirmedState.INSTANCE);
    }

    @Override
    public void cancel(Order order) {
        order.transitionTo(CancelledState.INSTANCE);
    }
}
