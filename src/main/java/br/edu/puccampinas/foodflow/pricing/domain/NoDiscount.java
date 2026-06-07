package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;

/**
 * Ausencia de desconto. Evita tratar "sem desconto" como caso especial (null):
 * o codigo cliente sempre tem uma estrategia para chamar (Null Object).
 */
public final class NoDiscount implements DiscountStrategy {

    @Override
    public Money apply(Money subtotal) {
        return Money.zero();
    }
}
