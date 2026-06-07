package br.edu.puccampinas.foodflow.shared.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Raiz de agregado. Acumula eventos de dominio enquanto o estado muda; a camada
 * de aplicacao os recolhe com {@link #pullDomainEvents()} apos persistir, o que
 * mantem o dominio livre de qualquer dependencia de infraestrutura de mensageria.
 */
public abstract class AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(Objects.requireNonNull(event, "event nao pode ser nulo"));
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> pending = List.copyOf(domainEvents);
        domainEvents.clear();
        return pending;
    }

    public boolean hasPendingEvents() {
        return !domainEvents.isEmpty();
    }
}
