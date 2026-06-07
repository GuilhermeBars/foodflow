/**
 * Precificacao de pedidos. As politicas de frete e de desconto sao modeladas com
 * o padrao Strategy: {@link br.edu.puccampinas.foodflow.pricing.domain.OrderPricing}
 * combina uma {@link br.edu.puccampinas.foodflow.pricing.domain.DeliveryFeeStrategy}
 * e uma {@link br.edu.puccampinas.foodflow.pricing.domain.DiscountStrategy} sem
 * conhecer suas implementacoes concretas.
 */
package br.edu.puccampinas.foodflow.pricing.domain;
