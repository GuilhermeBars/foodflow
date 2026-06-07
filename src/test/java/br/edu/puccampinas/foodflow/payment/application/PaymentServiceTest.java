package br.edu.puccampinas.foodflow.payment.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentGateway;
import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentRecordRepository;
import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import br.edu.puccampinas.foodflow.payment.domain.PaymentStatus;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PaymentServiceTest {

    /** Gateway que conta quantas vezes cobrou, para checar a idempotencia. */
    private static final class CountingGateway implements PaymentGateway {
        private int charges = 0;

        @Override
        public PaymentMethod supportedMethod() {
            return PaymentMethod.PIX;
        }

        @Override
        public PaymentResult charge(PaymentRequest request) {
            charges++;
            return new PaymentResult("TX-" + charges, PaymentStatus.APPROVED, "ok");
        }
    }

    private static final class InMemoryRecords implements PaymentRecordRepository {
        private final Map<String, PaymentResult> store = new HashMap<>();

        @Override
        public Optional<PaymentResult> findByIdempotencyKey(String key) {
            return Optional.ofNullable(store.get(key));
        }

        @Override
        public void save(String key, PaymentResult result) {
            store.put(key, result);
        }
    }

    private PaymentService serviceWith(CountingGateway gateway, InMemoryRecords records) {
        return new PaymentService(new PaymentGatewayFactory(java.util.List.of(gateway)), records);
    }

    @Test
    void mesmaChaveNaoCobraDuasVezes() {
        CountingGateway gateway = new CountingGateway();
        PaymentService service = serviceWith(gateway, new InMemoryRecords());
        PaymentRequest request = new PaymentRequest(
                UUID.randomUUID(), Money.brl("50.00"), PaymentMethod.PIX, "chave-1");

        PaymentResult first = service.pay(request);
        PaymentResult second = service.pay(request);

        assertEquals(first, second);
        assertEquals(1, gateway.charges);
    }

    @Test
    void chavesDiferentesGeramCobrancasSeparadas() {
        CountingGateway gateway = new CountingGateway();
        PaymentService service = serviceWith(gateway, new InMemoryRecords());
        UUID orderId = UUID.randomUUID();

        service.pay(new PaymentRequest(orderId, Money.brl("50.00"), PaymentMethod.PIX, "chave-a"));
        service.pay(new PaymentRequest(orderId, Money.brl("50.00"), PaymentMethod.PIX, "chave-b"));

        assertEquals(2, gateway.charges);
    }
}
