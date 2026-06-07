package br.edu.puccampinas.foodflow.payment.application.port.in;

import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;

/** Caso de uso: processar a cobranca de um pedido. */
public interface ProcessPaymentUseCase {

    PaymentResult pay(PaymentRequest request);
}
