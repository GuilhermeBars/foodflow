package br.edu.puccampinas.foodflow.notification.application;

import br.edu.puccampinas.foodflow.ordering.domain.OrderStatus;
import br.edu.puccampinas.foodflow.ordering.domain.OrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Avisa a cozinha do restaurante quando um pedido e confirmado. Cada observador
 * decide sozinho a quais transicoes reage.
 */
@Component
class RestaurantNotifier {

    private static final Logger log = LoggerFactory.getLogger(RestaurantNotifier.class);

    @EventListener
    void onStatusChanged(OrderStatusChangedEvent event) {
        if (event.newStatus() == OrderStatus.CONFIRMED) {
            log.info("Notificando restaurante: novo pedido {} para preparar", event.orderId());
        }
    }
}
