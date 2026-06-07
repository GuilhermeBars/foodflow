package br.edu.puccampinas.foodflow.ordering.domain;

import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.Objects;
import java.util.UUID;

/**
 * Linha do pedido. Guarda um retrato (nome e preco) do item no momento da compra,
 * para que alteracoes posteriores no cardapio nao mudem pedidos ja realizados.
 */
public class OrderItem {

    private final UUID menuItemId;
    private final String name;
    private final Money unitPrice;
    private final int quantity;

    public OrderItem(UUID menuItemId, String name, Money unitPrice, int quantity) {
        this.menuItemId = Objects.requireNonNull(menuItemId, "menuItemId");
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do item e obrigatorio");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        if (unitPrice.isNegative()) {
            throw new IllegalArgumentException("Preco unitario nao pode ser negativo");
        }
        this.name = name.trim();
        this.quantity = quantity;
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    public UUID menuItemId() {
        return menuItemId;
    }

    public String name() {
        return name;
    }

    public Money unitPrice() {
        return unitPrice;
    }

    public int quantity() {
        return quantity;
    }
}
