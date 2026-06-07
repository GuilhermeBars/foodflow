package br.edu.puccampinas.foodflow.payment.infrastructure;

import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentGateway;
import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import br.edu.puccampinas.foodflow.payment.domain.PaymentStatus;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** Adapter para o provedor de Pix (simulado). Aprova toda cobranca valida. */
@Component
class PixGateway implements PaymentGateway {

    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.PIX;
    }

    @Override
    public PaymentResult charge(PaymentRequest request) {
        return new PaymentResult("PIX-" + UUID.randomUUID(), PaymentStatus.APPROVED, "Pagamento via Pix aprovado");
    }
}
