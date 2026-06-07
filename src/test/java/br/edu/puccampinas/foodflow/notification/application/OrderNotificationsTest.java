package br.edu.puccampinas.foodflow.notification.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.edu.puccampinas.foodflow.ordering.domain.OrderStatus;
import br.edu.puccampinas.foodflow.ordering.domain.OrderStatusChangedEvent;
import br.edu.puccampinas.foodflow.shared.application.DomainEventPublisher;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderNotificationsTest {

    @Autowired
    private DomainEventPublisher publisher;

    @Autowired
    private AnalyticsListener analytics;

    @Test
    void observadoresReagemAoEventoDeStatus() {
        int before = analytics.processedCount();

        publisher.publish(List.of(new OrderStatusChangedEvent(
                UUID.randomUUID(), OrderStatus.CREATED, OrderStatus.CONFIRMED, Instant.now())));

        assertEquals(before + 1, analytics.processedCount());
    }
}
