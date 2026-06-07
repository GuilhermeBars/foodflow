package br.edu.puccampinas.foodflow.shared.domain;

/**
 * Violacao de uma regra de negocio sobre um estado que existe (ex.: avancar um
 * pedido ja cancelado). Mapeada para HTTP 409/422 na borda da aplicacao.
 */
public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
