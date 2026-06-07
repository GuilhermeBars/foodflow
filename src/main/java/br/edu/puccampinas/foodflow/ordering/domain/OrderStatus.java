package br.edu.puccampinas.foodflow.ordering.domain;

/** Situacao do pedido exposta para persistencia e para a API. */
public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}
