package br.edu.puccampinas.foodflow.payment.application;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentGateway;
import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import br.edu.puccampinas.foodflow.payment.domain.PaymentStatus;
import br.edu.puccampinas.foodflow.shared.domain.BusinessRuleException;
import java.util.List;
import org.junit.jupiter.api.Test;

class PaymentGatewayFactoryTest {

    private static PaymentGateway gatewayFor(PaymentMethod method) {
        return new PaymentGateway() {
            @Override
            public PaymentMethod supportedMethod() {
                return method;
            }

            @Override
            public PaymentResult charge(PaymentRequest request) {
                return new PaymentResult("TX", PaymentStatus.APPROVED, "ok");
            }
        };
    }

    @Test
    void selecionaOGatewayDoMetodoSolicitado() {
        PaymentGateway pix = gatewayFor(PaymentMethod.PIX);
        PaymentGateway card = gatewayFor(PaymentMethod.CREDIT_CARD);
        PaymentGatewayFactory factory = new PaymentGatewayFactory(List.of(pix, card));

        assertSame(pix, factory.forMethod(PaymentMethod.PIX));
        assertSame(card, factory.forMethod(PaymentMethod.CREDIT_CARD));
    }

    @Test
    void falhaQuandoNaoHaGatewayParaOMetodo() {
        PaymentGatewayFactory factory = new PaymentGatewayFactory(List.of(gatewayFor(PaymentMethod.PIX)));
        assertThrows(BusinessRuleException.class, () -> factory.forMethod(PaymentMethod.CREDIT_CARD));
    }
}
