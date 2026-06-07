package br.edu.puccampinas.foodflow.catalog.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Modelo de persistencia do restaurante. Vive na infraestrutura, nao no dominio. */
@Entity
@Table(name = "restaurants")
class RestaurantJpaEntity {

    @Id
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String cuisine;

    boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    List<MenuItemJpaEntity> menu = new ArrayList<>();

    protected RestaurantJpaEntity() {
    }

    RestaurantJpaEntity(UUID id, String name, String cuisine, boolean active, List<MenuItemJpaEntity> menu) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.active = active;
        this.menu = menu;
    }
}
