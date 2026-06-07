package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.math.BigDecimal;
import java.util.Objects;

/** Desconto percentual sobre o subtotal (ex.: cupom de 15%). */
public final class CouponPercentageDiscount implements DiscountStrategy {

    private final BigDecimal percentage;

    public CouponPercentageDiscount(BigDecimal percentage) {
        this.percentage = Objects.requireNonNull(percentage, "percentage");
        if (percentage.signum() < 0 || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentual de desconto deve estar entre 0 e 100");
        }
    }

    @Override
    public Money apply(Money subtotal) {
        return subtotal.multiply(percentage.movePointLeft(2));
    }
}
