package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;

/**
 * Estrategia de desconto (padrao Strategy, GoF). Retorna o valor a ser abatido
 * do subtotal; nunca negativo.
 */
public interface DiscountStrategy {

    Money apply(Money subtotal);
}
