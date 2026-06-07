package br.edu.puccampinas.foodflow.ordering.domain;

import br.edu.puccampinas.foodflow.shared.domain.DomainEvent;
import java.time.Instant;
import java.util.UUID;

/**
 * Publicado a cada transicao de estado do pedido. O modulo de notificacao
 * reage a ele sem que o modulo de pedidos conheca os observadores (Observer).
 */
public record OrderStatusChangedEvent(
        UUID orderId,
        OrderStatus previousStatus,
        OrderStatus newStatus,
        Instant occurredOn) implements DomainEvent {
}
