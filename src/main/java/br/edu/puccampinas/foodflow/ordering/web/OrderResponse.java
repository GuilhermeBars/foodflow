package br.edu.puccampinas.foodflow.ordering.web;

import br.edu.puccampinas.foodflow.ordering.domain.Order;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID restaurantId,
        String customerId,
        String status,
        List<OrderItemResponse> items,
        BigDecimal subtotal) {

    static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.items().stream()
                .map(OrderItemResponse::from)
                .toList();
        return new OrderResponse(
                order.id(),
                order.restaurantId(),
                order.customerId(),
                order.status().name(),
                items,
                order.subtotal().amount());
    }
}
