package br.edu.puccampinas.foodflow.shared.domain;

/**
 * Um recurso referenciado nao existe (ex.: pedido inexistente). Mapeada para
 * HTTP 404 na borda da aplicacao.
 */
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
