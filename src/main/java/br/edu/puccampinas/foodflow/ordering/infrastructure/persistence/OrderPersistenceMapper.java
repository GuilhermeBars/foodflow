package br.edu.puccampinas.foodflow.ordering.infrastructure.persistence;

import br.edu.puccampinas.foodflow.ordering.domain.Order;
import br.edu.puccampinas.foodflow.ordering.domain.OrderItem;
import br.edu.puccampinas.foodflow.ordering.domain.OrderStatus;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

/** Traducao entre o agregado Order e seu modelo de persistencia. */
final class OrderPersistenceMapper {

    private OrderPersistenceMapper() {
    }

    static OrderJpaEntity toJpa(Order order) {
        List<OrderItemJpaEntity> items = new ArrayList<>();
        for (OrderItem item : order.items()) {
            items.add(toJpa(item));
        }
        return new OrderJpaEntity(
                order.id(), order.restaurantId(), order.customerId(), order.status().name(), items);
    }

    private static OrderItemJpaEntity toJpa(OrderItem item) {
        return new OrderItemJpaEntity(
                UUID.randomUUID(),
                item.menuItemId(),
                item.name(),
                item.unitPrice().amount(),
                item.unitPrice().currency().getCurrencyCode(),
                item.quantity());
    }

    static Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.items.stream()
                .map(OrderPersistenceMapper::toDomain)
                .toList();
        return Order.reconstitute(
                entity.id, entity.restaurantId, entity.customerId, items, OrderStatus.valueOf(entity.status));
    }

    private static OrderItem toDomain(OrderItemJpaEntity entity) {
        Money unitPrice = new Money(entity.unitPrice, Currency.getInstance(entity.currency));
        return new OrderItem(entity.menuItemId, entity.name, unitPrice, entity.quantity);
    }
}
