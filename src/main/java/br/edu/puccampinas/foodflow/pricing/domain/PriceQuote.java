package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;

/** Detalhamento do preco de um pedido: subtotal, frete, desconto e total. */
public record PriceQuote(Money subtotal, Money deliveryFee, Money discount, Money total) {
}
