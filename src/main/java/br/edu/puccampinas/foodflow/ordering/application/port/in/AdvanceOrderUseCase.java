package br.edu.puccampinas.foodflow.ordering.application.port.in;

import br.edu.puccampinas.foodflow.ordering.domain.Order;
import java.util.UUID;

/** Caso de uso: avancar um pedido para o proximo estado (ou cancela-lo). */
public interface AdvanceOrderUseCase {

    Order apply(UUID orderId, OrderTransition transition);
}
