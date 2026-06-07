package br.edu.puccampinas.foodflow.shared.domain;

import java.time.Instant;

/**
 * Algo relevante que aconteceu no dominio e que outros modulos podem observar.
 * A publicacao desacopla quem provoca o fato de quem reage a ele.
 */
public interface DomainEvent {

    Instant occurredOn();
}
