package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.Objects;

/** Frete de valor fixo, independente de distancia ou valor do pedido. */
public final class FixedDeliveryFee implements DeliveryFeeStrategy {

    private final Money fee;

    public FixedDeliveryFee(Money fee) {
        this.fee = Objects.requireNonNull(fee, "fee");
        if (fee.isNegative()) {
            throw new IllegalArgumentException("Frete nao pode ser negativo");
        }
    }

    @Override
    public Money calculate(Money subtotal, double distanceKm) {
        return fee;
    }
}
