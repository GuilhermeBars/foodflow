package br.edu.puccampinas.foodflow.catalog.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.Objects;
import java.util.UUID;

/** Item de cardapio de um restaurante. */
public class MenuItem {

    private final UUID id;
    private String name;
    private String description;
    private Money price;
    private boolean available;

    private MenuItem(UUID id, String name, String description, Money price, boolean available) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = requireText(name);
        this.description = description == null ? "" : description.trim();
        this.price = requireNonNegative(price);
        this.available = available;
    }

    public static MenuItem create(String name, String description, Money price) {
        return new MenuItem(UUID.randomUUID(), name, description, price, true);
    }

    public static MenuItem reconstitute(UUID id, String name, String description, Money price, boolean available) {
        return new MenuItem(id, name, description, price, available);
    }

    public void changePrice(Money newPrice) {
        this.price = requireNonNegative(newPrice);
    }

    public void makeUnavailable() {
        this.available = false;
    }

    private static Money requireNonNegative(Money price) {
        Objects.requireNonNull(price, "preco");
        if (price.isNegative()) {
            throw new IllegalArgumentException("Preco do item nao pode ser negativo");
        }
        return price;
    }

    private static String requireText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Nome do item e obrigatorio");
        }
        return value.trim();
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Money price() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }
}
