package br.edu.puccampinas.foodflow.payment.infrastructure.persistence;

import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentRecordRepository;
import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import br.edu.puccampinas.foodflow.payment.domain.PaymentStatus;
import java.util.Optional;
import org.springframework.stereotype.Component;

/** Adapter JPA da porta de idempotencia de pagamentos. */
@Component
class PaymentRecordRepositoryAdapter implements PaymentRecordRepository {

    private final SpringDataPaymentRecordRepository jpa;

    PaymentRecordRepositoryAdapter(SpringDataPaymentRecordRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<PaymentResult> findByIdempotencyKey(String idempotencyKey) {
        return jpa.findById(idempotencyKey)
                .map(entity -> new PaymentResult(
                        entity.transactionId, PaymentStatus.valueOf(entity.status), entity.message));
    }

    @Override
    public void save(String idempotencyKey, PaymentResult result) {
        jpa.save(new PaymentRecordJpaEntity(
                idempotencyKey, result.transactionId(), result.status().name(), result.message()));
    }
}
