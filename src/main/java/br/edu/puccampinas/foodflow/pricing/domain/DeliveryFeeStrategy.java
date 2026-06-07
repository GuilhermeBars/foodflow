package br.edu.puccampinas.foodflow.pricing.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;

/**
 * Estrategia de calculo do frete (padrao Strategy, GoF). Trocar a politica de
 * frete passa a ser uma questao de escolher outra implementacao, sem tocar em
 * quem calcula o total do pedido (OCP).
 */
public interface DeliveryFeeStrategy {

    Money calculate(Money subtotal, double distanceKm);
}
