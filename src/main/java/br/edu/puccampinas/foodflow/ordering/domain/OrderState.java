package br.edu.puccampinas.foodflow.ordering.domain;

import br.edu.puccampinas.foodflow.shared.domain.BusinessRuleException;

/**
 * Estado do pedido (padrao State, GoF). Cada estado concreto sabe quais
 * transicoes sao validas a partir dele; as demais caem no comportamento padrao
 * abaixo, que rejeita a operacao. Assim o {@link Order} nao precisa de nenhum
 * {@code if/switch} sobre o status: ele apenas delega ao estado atual.
 */
public interface OrderState {

    OrderStatus status();

    default void confirm(Order order) {
        throw illegalTransition("confirmar");
    }

    default void startPreparing(Order order) {
        throw illegalTransition("iniciar o preparo de");
    }

    default void dispatch(Order order) {
        throw illegalTransition("despachar");
    }

    default void deliver(Order order) {
        throw illegalTransition("entregar");
    }

    default void cancel(Order order) {
        throw illegalTransition("cancelar");
    }

    default boolean isTerminal() {
        return false;
    }

    private BusinessRuleException illegalTransition(String action) {
        return new BusinessRuleException(
                "Nao e possivel " + action + " um pedido com status " + status());
    }

    /** Reconstroi o estado a partir do status persistido. */
    static OrderState forStatus(OrderStatus status) {
        return switch (status) {
            case CREATED -> CreatedState.INSTANCE;
            case CONFIRMED -> ConfirmedState.INSTANCE;
            case PREPARING -> PreparingState.INSTANCE;
            case OUT_FOR_DELIVERY -> OutForDeliveryState.INSTANCE;
            case DELIVERED -> DeliveredState.INSTANCE;
            case CANCELLED -> CancelledState.INSTANCE;
        };
    }
}
