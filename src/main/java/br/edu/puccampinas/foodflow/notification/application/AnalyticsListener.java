package br.edu.puccampinas.foodflow.notification.application;

import br.edu.puccampinas.foodflow.ordering.domain.OrderStatusChangedEvent;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Contabiliza as transicoes de pedido para fins de metrica. */
@Component
class AnalyticsListener {

    private final AtomicInteger processedEvents = new AtomicInteger();

    @EventListener
    void onStatusChanged(OrderStatusChangedEvent event) {
        processedEvents.incrementAndGet();
    }

    int processedCount() {
        return processedEvents.get();
    }
}
