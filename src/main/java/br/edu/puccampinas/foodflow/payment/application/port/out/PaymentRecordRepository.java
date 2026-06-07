package br.edu.puccampinas.foodflow.payment.application.port.out;

import br.edu.puccampinas.foodflow.payment.domain.PaymentResult;
import java.util.Optional;

/** Guarda o resultado de cada cobranca por chave de idempotencia. */
public interface PaymentRecordRepository {

    Optional<PaymentResult> findByIdempotencyKey(String idempotencyKey);

    void save(String idempotencyKey, PaymentResult result);
}
