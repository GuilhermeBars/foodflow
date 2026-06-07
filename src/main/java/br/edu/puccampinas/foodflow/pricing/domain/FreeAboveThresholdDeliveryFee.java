package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.Objects;

/**
 * Frete gratis quando o subtotal atinge um limite; abaixo dele, delega a outra
 * estrategia. Compor estrategias evita duplicar a regra do limite.
 */
public final class FreeAboveThresholdDeliveryFee implements DeliveryFeeStrategy {

    private final Money threshold;
    private final DeliveryFeeStrategy below;

    public FreeAboveThresholdDeliveryFee(Money threshold, DeliveryFeeStrategy below) {
        this.threshold = Objects.requireNonNull(threshold, "threshold");
        this.below = Objects.requireNonNull(below, "below");
    }

    @Override
    public Money calculate(Money subtotal, double distanceKm) {
        if (subtotal.isGreaterThanOrEqual(threshold)) {
            return Money.zero();
        }
        return below.calculate(subtotal, distanceKm);
    }
}
