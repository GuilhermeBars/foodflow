package br.edu.puccampinas.foodflow.ordering.domain;

/** Estado terminal: pedido entregue, nenhuma transicao adicional e permitida. */
final class DeliveredState implements OrderState {

    static final DeliveredState INSTANCE = new DeliveredState();

    private DeliveredState() {
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.DELIVERED;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
