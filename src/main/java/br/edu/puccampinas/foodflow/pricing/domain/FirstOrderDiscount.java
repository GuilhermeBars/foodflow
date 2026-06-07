package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.math.BigDecimal;
import java.util.Objects;

/** Desconto de primeira compra: percentual limitado a um teto em reais. */
public final class FirstOrderDiscount implements DiscountStrategy {

    private final BigDecimal percentage;
    private final Money cap;

    public FirstOrderDiscount(BigDecimal percentage, Money cap) {
        this.percentage = Objects.requireNonNull(percentage, "percentage");
        this.cap = Objects.requireNonNull(cap, "cap");
        if (percentage.signum() < 0) {
            throw new IllegalArgumentException("Percentual de desconto nao pode ser negativo");
        }
    }

    @Override
    public Money apply(Money subtotal) {
        Money raw = subtotal.multiply(percentage.movePointLeft(2));
        return raw.isGreaterThan(cap) ? cap : raw;
    }
}
