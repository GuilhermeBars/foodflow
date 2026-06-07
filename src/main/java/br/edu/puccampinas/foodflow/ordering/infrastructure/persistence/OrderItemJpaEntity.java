package br.edu.puccampinas.foodflow.ordering.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/** Linha persistida do pedido. Possui PK propria, separada do id do item de cardapio. */
@Entity
@Table(name = "order_items")
class OrderItemJpaEntity {

    @Id
    UUID id;

    @Column(nullable = false)
    UUID menuItemId;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    BigDecimal unitPrice;

    @Column(nullable = false, length = 3)
    String currency;

    int quantity;

    protected OrderItemJpaEntity() {
    }

    OrderItemJpaEntity(UUID id, UUID menuItemId, String name, BigDecimal unitPrice, String currency, int quantity) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.currency = currency;
        this.quantity = quantity;
    }
}
