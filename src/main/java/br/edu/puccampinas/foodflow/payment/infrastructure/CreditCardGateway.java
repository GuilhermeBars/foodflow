package br.edu.puccampinas.foodflow.payment.infrastructure;

import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentGateway;
import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import br.edu.puccampinas.foodflow.payment.domain.PaymentStatus;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** Adapter para a adquirente de cartao (simulada). Recusa valores acima do limite. */
@Component
class CreditCardGateway implements PaymentGateway {

    private static final Money LIMIT = Money.brl("5000.00");

    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public PaymentResult charge(PaymentRequest request) {
        String transactionId = "CC-" + UUID.randomUUID();
        if (request.amount().isGreaterThan(LIMIT)) {
            return new PaymentResult(transactionId, PaymentStatus.DECLINED, "Valor acima do limite do cartao");
        }
        return new PaymentResult(transactionId, PaymentStatus.APPROVED, "Pagamento no cartao aprovado");
    }
}
