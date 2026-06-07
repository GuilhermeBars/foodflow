package br.edu.puccampinas.foodflow.pricing.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricingTest {

    @Test
    void freteFixoIgnoraDistanciaEValor() {
        DeliveryFeeStrategy frete = new FixedDeliveryFee(Money.brl("7.00"));
        assertEquals(Money.brl("7.00"), frete.calculate(Money.brl("20.00"), 12.0));
    }

    @Test
    void fretePorDistanciaSomaBaseMaisPorKm() {
        DeliveryFeeStrategy frete = new DistanceBasedDeliveryFee(Money.brl("5.00"), Money.brl("1.50"));
        assertEquals(Money.brl("12.50"), frete.calculate(Money.brl("30.00"), 5.0));
    }

    @Test
    void freteGratisAcimaDoLimiteSenaoDelega() {
        DeliveryFeeStrategy frete = new FreeAboveThresholdDeliveryFee(
                Money.brl("60.00"), new FixedDeliveryFee(Money.brl("9.90")));
        assertEquals(Money.zero(), frete.calculate(Money.brl("75.00"), 3.0));
        assertEquals(Money.brl("9.90"), frete.calculate(Money.brl("40.00"), 3.0));
    }

    @Test
    void cupomAplicaPercentualSobreOSubtotal() {
        DiscountStrategy cupom = new CouponPercentageDiscount(BigDecimal.valueOf(15));
        assertEquals(Money.brl("15.00"), cupom.apply(Money.brl("100.00")));
    }

    @Test
    void primeiraCompraRespeitaOTeto() {
        DiscountStrategy desconto = new FirstOrderDiscount(BigDecimal.valueOf(50), Money.brl("20.00"));
        assertEquals(Money.brl("20.00"), desconto.apply(Money.brl("100.00")));
        assertEquals(Money.brl("15.00"), desconto.apply(Money.brl("30.00")));
    }

    @Test
    void totalSomaSubtotalEFreteDescontandoDesconto() {
        OrderPricing pricing = new OrderPricing();
        PriceQuote quote = pricing.quote(
                Money.brl("100.00"),
                4.0,
                new FixedDeliveryFee(Money.brl("8.00")),
                new CouponPercentageDiscount(BigDecimal.valueOf(10)));

        assertEquals(Money.brl("100.00"), quote.subtotal());
        assertEquals(Money.brl("8.00"), quote.deliveryFee());
        assertEquals(Money.brl("10.00"), quote.discount());
        assertEquals(Money.brl("98.00"), quote.total());
    }

    @Test
    void totalNuncaFicaNegativo() {
        OrderPricing pricing = new OrderPricing();
        PriceQuote quote = pricing.quote(
                Money.brl("10.00"),
                0.0,
                new FixedDeliveryFee(Money.zero()),
                new FirstOrderDiscount(BigDecimal.valueOf(100), Money.brl("999.00")));

        assertEquals(Money.zero(), quote.total());
    }
}
