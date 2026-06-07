package br.edu.puccampinas.foodflow.ordering.web;

import br.edu.puccampinas.foodflow.ordering.domain.OrderItem;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID menuItemId,
        String name,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal) {

    static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.menuItemId(),
                item.name(),
                item.unitPrice().amount(),
                item.quantity(),
                item.subtotal().amount());
    }
}
