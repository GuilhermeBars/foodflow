package br.edu.puccampinas.foodflow.shared.application;

import br.edu.puccampinas.foodflow.shared.domain.AggregateRoot;
import br.edu.puccampinas.foodflow.shared.domain.DomainEvent;
import java.util.Collection;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Entrega os eventos de dominio ao mecanismo de eventos do Spring. Os modulos
 * que produzem eventos dependem apenas desta fachada; quem os observa permanece
 * desconhecido para eles (Observer).
 */
@Component
public class DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public DomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(Collection<? extends DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }

    /** Publica e limpa os eventos acumulados por um agregado apos persisti-lo. */
    public void publishEventsOf(AggregateRoot aggregate) {
        publish(aggregate.pullDomainEvents());
    }
}
