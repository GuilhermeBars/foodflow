package br.edu.puccampinas.foodflow.ordering.domain;

/** Estado terminal: pedido cancelado, nenhuma transicao adicional e permitida. */
final class CancelledState implements OrderState {

    static final CancelledState INSTANCE = new CancelledState();

    private CancelledState() {
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.CANCELLED;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
