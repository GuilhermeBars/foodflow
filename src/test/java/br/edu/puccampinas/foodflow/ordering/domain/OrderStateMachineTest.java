package br.edu.puccampinas.foodflow.ordering.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.edu.puccampinas.foodflow.shared.domain.BusinessRuleException;
import br.edu.puccampinas.foodflow.shared.domain.DomainEvent;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrderStateMachineTest {

    private Order newOrder() {
        OrderItem item = new OrderItem(UUID.randomUUID(), "Pizza Margherita", Money.brl("49.90"), 2);
        return Order.place(UUID.randomUUID(), "cliente-1", List.of(item));
    }

    @Test
    void caminhoFelizPercorreTodosOsEstados() {
        Order order = newOrder();
        assertEquals(OrderStatus.CREATED, order.status());

        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.status());

        order.startPreparing();
        assertEquals(OrderStatus.PREPARING, order.status());

        order.dispatch();
        assertEquals(OrderStatus.OUT_FOR_DELIVERY, order.status());

        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.status());
        assertTrue(order.isFinished());
    }

    @Test
    void naoEntregaUmPedidoQueAindaNaoSaiuParaEntrega() {
        Order order = newOrder();
        assertThrows(BusinessRuleException.class, order::deliver);
        assertThrows(BusinessRuleException.class, order::dispatch);
        assertThrows(BusinessRuleException.class, order::startPreparing);
    }

    @Test
    void naoDespachaUmPedidoApenasConfirmado() {
        Order order = newOrder();
        order.confirm();
        assertThrows(BusinessRuleException.class, order::dispatch);
    }

    @Test
    void permiteCancelarAteOPreparo() {
        Order order = newOrder();
        order.confirm();
        order.startPreparing();
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.status());
        assertTrue(order.isFinished());
    }

    @Test
    void naoReativaUmPedidoEmEstadoTerminal() {
        Order delivered = newOrder();
        delivered.confirm();
        delivered.startPreparing();
        delivered.dispatch();
        delivered.deliver();
        assertThrows(BusinessRuleException.class, delivered::cancel);
        assertThrows(BusinessRuleException.class, delivered::confirm);

        Order cancelled = newOrder();
        cancelled.cancel();
        assertThrows(BusinessRuleException.class, cancelled::confirm);
    }

    @Test
    void cadaTransicaoPublicaUmEvento() {
        Order order = newOrder();
        order.confirm();

        List<DomainEvent> events = order.pullDomainEvents();
        assertEquals(2, events.size());
        OrderStatusChangedEvent confirmacao = (OrderStatusChangedEvent) events.get(1);
        assertEquals(OrderStatus.CREATED, confirmacao.previousStatus());
        assertEquals(OrderStatus.CONFIRMED, confirmacao.newStatus());

        assertFalse(order.hasPendingEvents());
    }

    @Test
    void subtotalSomaOsItens() {
        OrderItem pizza = new OrderItem(UUID.randomUUID(), "Pizza", Money.brl("49.90"), 2);
        OrderItem refri = new OrderItem(UUID.randomUUID(), "Refrigerante", Money.brl("8.00"), 3);
        Order order = Order.place(UUID.randomUUID(), "cliente-1", List.of(pizza, refri));
        assertEquals(Money.brl("123.80"), order.subtotal());
    }
}
