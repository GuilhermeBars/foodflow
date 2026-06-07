package br.edu.puccampinas.foodflow.ordering.infrastructure.persistence;

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

/** Modelo de persistencia do pedido. */
@Entity
@Table(name = "orders")
class OrderJpaEntity {

    @Id
    UUID id;

    @Column(nullable = false)
    UUID restaurantId;

    @Column(nullable = false)
    String customerId;

    @Column(nullable = false)
    String status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    List<OrderItemJpaEntity> items = new ArrayList<>();

    protected OrderJpaEntity() {
    }

    OrderJpaEntity(UUID id, UUID restaurantId, String customerId, String status, List<OrderItemJpaEntity> items) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.status = status;
        this.items = items;
    }
}
