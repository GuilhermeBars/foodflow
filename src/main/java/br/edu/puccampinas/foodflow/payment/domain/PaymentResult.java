package br.edu.puccampinas.foodflow.payment.domain;

/** Resultado de uma cobranca, devolvido pelo gateway e guardado para idempotencia. */
public record PaymentResult(String transactionId, PaymentStatus status, String message) {

    public boolean isApproved() {
        return status == PaymentStatus.APPROVED;
    }
}
