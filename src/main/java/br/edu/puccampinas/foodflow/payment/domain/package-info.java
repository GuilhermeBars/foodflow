/**
 * Pagamentos. Integra com provedores externos atraves de uma porta
 * ({@code PaymentGateway}): cada provedor e um Adapter, e uma Factory escolhe o
 * adapter conforme a forma de pagamento. O servico aplica idempotencia para que
 * reenvios nao gerem cobranca dupla.
 */
package br.edu.puccampinas.foodflow.payment.domain;
