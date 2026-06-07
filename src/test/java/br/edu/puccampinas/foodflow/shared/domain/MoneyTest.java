package br.edu.puccampinas.foodflow.shared.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void somaValoresNaMesmaMoeda() {
        assertEquals(Money.brl("15.50"), Money.brl("10.00").add(Money.brl("5.50")));
    }

    @Test
    void multiplicaPorQuantidade() {
        assertEquals(Money.brl("37.50"), Money.brl("12.50").multiply(3));
    }

    @Test
    void normalizaParaDuasCasasDecimais() {
        assertEquals(0, Money.brl(new BigDecimal("10.1")).amount().compareTo(new BigDecimal("10.10")));
        assertEquals(2, Money.brl(new BigDecimal("10.1")).amount().scale());
    }

    @Test
    void comparaValores() {
        assertTrue(Money.brl("10.00").isGreaterThan(Money.brl("9.99")));
        assertFalse(Money.brl("10.00").isGreaterThan(Money.brl("10.00")));
    }

    @Test
    void rejeitaOperacaoEntreMoedasDiferentes() {
        Money real = Money.brl("10.00");
        Money dolar = new Money(new BigDecimal("10.00"), java.util.Currency.getInstance("USD"));
        assertThrows(IllegalArgumentException.class, () -> real.add(dolar));
    }
}
