package br.edu.puccampinas.foodflow.payment.application.port.out;

import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;

/**
 * Porta para um provedor de pagamento externo. Cada implementacao (Adapter)
 * traduz o contrato do dominio para um provedor especifico e declara qual forma
 * de pagamento atende.
 */
public interface PaymentGateway {

    PaymentMethod supportedMethod();

    PaymentResult charge(PaymentRequest request);
}
