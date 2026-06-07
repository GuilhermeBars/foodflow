package br.edu.puccampinas.foodflow.payment.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Registro de cobranca indexado pela chave de idempotencia (a propria PK). */
@Entity
@Table(name = "payment_records")
class PaymentRecordJpaEntity {

    @Id
    String idempotencyKey;

    @Column(nullable = false)
    String transactionId;

    @Column(nullable = false)
    String status;

    String message;

    protected PaymentRecordJpaEntity() {
    }

    PaymentRecordJpaEntity(String idempotencyKey, String transactionId, String status, String message) {
        this.idempotencyKey = idempotencyKey;
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
    }
}
