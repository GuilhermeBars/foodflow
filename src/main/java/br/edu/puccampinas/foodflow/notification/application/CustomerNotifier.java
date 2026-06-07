package br.edu.puccampinas.foodflow.notification.application;

import br.edu.puccampinas.foodflow.ordering.domain.OrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Avisa o cliente a cada mudanca de status do pedido. */
@Component
class CustomerNotifier {

    private static final Logger log = LoggerFactory.getLogger(CustomerNotifier.class);

    @EventListener
    void onStatusChanged(OrderStatusChangedEvent event) {
        log.info("Notificando cliente: pedido {} agora esta {}", event.orderId(), event.newStatus());
    }
}
