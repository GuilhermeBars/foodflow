package br.edu.puccampinas.foodflow.shared.domain;

/**
 * Raiz das excecoes de regra de negocio. Sinaliza que uma invariante do dominio
 * foi violada, em oposicao a falhas tecnicas. A camada web a traduz em respostas
 * HTTP apropriadas.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
