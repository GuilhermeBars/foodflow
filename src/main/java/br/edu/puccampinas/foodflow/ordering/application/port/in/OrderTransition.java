package br.edu.puccampinas.foodflow.ordering.application.port.in;

/** Acoes que avancam (ou encerram) o ciclo de vida de um pedido. */
public enum OrderTransition {
    CONFIRM,
    START_PREPARING,
    DISPATCH,
    DELIVER,
    CANCEL
}
