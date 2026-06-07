package br.edu.puccampinas.foodflow.ordering.infrastructure.persistence;

import br.edu.puccampinas.foodflow.ordering.application.port.out.OrderRepository;
import br.edu.puccampinas.foodflow.ordering.domain.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/** Adapter JPA da porta de pedidos. */
@Component
class OrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderRepository jpa;

    OrderRepositoryAdapter(SpringDataOrderRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = jpa.findById(order.id())
                .map(existing -> updateStatus(existing, order))
                .orElseGet(() -> OrderPersistenceMapper.toJpa(order));
        return OrderPersistenceMapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpa.findById(id).map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(OrderPersistenceMapper::toDomain);
    }

    /** Itens do pedido nao mudam apos a criacao; atualizar so o status evita duplicar linhas. */
    private OrderJpaEntity updateStatus(OrderJpaEntity existing, Order order) {
        existing.status = order.status().name();
        return existing;
    }
}
