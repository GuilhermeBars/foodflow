package br.edu.puccampinas.foodflow.catalog.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/** Modelo de persistencia do item de cardapio. */
@Entity
@Table(name = "menu_items")
class MenuItemJpaEntity {

    @Id
    UUID id;

    @Column(nullable = false)
    String name;

    String description;

    @Column(nullable = false)
    BigDecimal price;

    @Column(nullable = false, length = 3)
    String currency;

    boolean available;

    protected MenuItemJpaEntity() {
    }

    MenuItemJpaEntity(UUID id, String name, String description, BigDecimal price, String currency, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.available = available;
    }
}
