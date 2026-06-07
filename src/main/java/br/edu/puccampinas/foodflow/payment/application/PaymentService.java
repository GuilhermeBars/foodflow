package br.edu.puccampinas.foodflow.payment.application;

import br.edu.puccampinas.foodflow.payment.application.port.in.ProcessPaymentUseCase;
import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentRecordRepository;
import br.edu.puccampinas.foodflow.payment.domain.PaymentRequest;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processa cobrancas garantindo idempotencia: se a mesma chave ja foi cobrada,
 * devolve o resultado guardado em vez de chamar o gateway de novo.
 */
@Service
public class PaymentService implements ProcessPaymentUseCase {

    private final PaymentGatewayFactory gatewayFactory;
    private final PaymentRecordRepository records;

    public PaymentService(PaymentGatewayFactory gatewayFactory, PaymentRecordRepository records) {
        this.gatewayFactory = gatewayFactory;
        this.records = records;
    }

    @Override
    @Transactional
    public PaymentResult pay(PaymentRequest request) {
        Optional<PaymentResult> alreadyProcessed = records.findByIdempotencyKey(request.idempotencyKey());
        if (alreadyProcessed.isPresent()) {
            return alreadyProcessed.get();
        }
        PaymentResult result = gatewayFactory.forMethod(request.method()).charge(request);
        records.save(request.idempotencyKey(), result);
        return result;
    }
}
