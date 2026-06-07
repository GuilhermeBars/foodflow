package br.edu.puccampinas.foodflow.shared.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Valor monetario imutavel. Encapsula o {@link BigDecimal} e a moeda para
 * impedir erros classicos de ponto flutuante e de soma entre moedas diferentes.
 */
public record Money(BigDecimal amount, Currency currency) {

    private static final Currency BRL = Currency.getInstance("BRL");

    public Money {
        Objects.requireNonNull(amount, "amount nao pode ser nulo");
        Objects.requireNonNull(currency, "currency nao pode ser nula");
        amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
    }

    public static Money brl(BigDecimal amount) {
        return new Money(amount, BRL);
    }

    public static Money brl(String amount) {
        return brl(new BigDecimal(amount));
    }

    public static Money zero() {
        return brl(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(int quantity) {
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }

    public Money multiply(BigDecimal factor) {
        return new Money(amount.multiply(factor), currency);
    }

    public boolean isNegative() {
        return amount.signum() < 0;
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isGreaterThan(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) > 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) >= 0;
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Moedas incompativeis: %s e %s".formatted(currency, other.currency));
        }
    }

    @Override
    public String toString() {
        return "%s %.2f".formatted(currency.getCurrencyCode(), amount);
    }
}
