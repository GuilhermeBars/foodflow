package br.edu.puccampinas.foodflow.payment.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.Objects;
import java.util.UUID;

/**
 * Pedido de cobranca. A {@code idempotencyKey} garante que reenviar a mesma
 * requisicao (ex.: retry de rede) nao gere uma segunda cobranca.
 */
public record PaymentRequest(UUID orderId, Money amount, PaymentMethod method, String idempotencyKey) {

    public PaymentRequest {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(method, "method");
        if (amount.isNegative()) {
            throw new IllegalArgumentException("Valor da cobranca nao pode ser negativo");
        }
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Chave de idempotencia e obrigatoria");
        }
    }
}
