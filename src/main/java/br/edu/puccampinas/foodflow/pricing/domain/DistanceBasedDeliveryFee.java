package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.math.BigDecimal;
import java.util.Objects;

/** Frete proporcional a distancia: uma taxa base somada a um valor por quilometro. */
public final class DistanceBasedDeliveryFee implements DeliveryFeeStrategy {

    private final Money base;
    private final Money perKilometer;

    public DistanceBasedDeliveryFee(Money base, Money perKilometer) {
        this.base = Objects.requireNonNull(base, "base");
        this.perKilometer = Objects.requireNonNull(perKilometer, "perKilometer");
    }

    @Override
    public Money calculate(Money subtotal, double distanceKm) {
        if (distanceKm < 0) {
            throw new IllegalArgumentException("Distancia nao pode ser negativa");
        }
        return base.add(perKilometer.multiply(BigDecimal.valueOf(distanceKm)));
    }
}
