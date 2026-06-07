package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;

/**
 * Calcula o preco final do pedido combinando uma estrategia de frete e uma de
 * desconto. Este servico nunca muda quando surgem novas politicas: depende
 * apenas das abstracoes {@link DeliveryFeeStrategy} e {@link DiscountStrategy}.
 */
public final class OrderPricing {

    public PriceQuote quote(Money subtotal,
                            double distanceKm,
                            DeliveryFeeStrategy deliveryFeeStrategy,
                            DiscountStrategy discountStrategy) {
        Money deliveryFee = deliveryFeeStrategy.calculate(subtotal, distanceKm);
        Money payable = subtotal.add(deliveryFee);

        Money requestedDiscount = discountStrategy.apply(subtotal);
        Money discount = requestedDiscount.isGreaterThan(payable) ? payable : requestedDiscount;

        Money total = payable.subtract(discount);
        return new PriceQuote(subtotal, deliveryFee, discount, total);
    }
}
