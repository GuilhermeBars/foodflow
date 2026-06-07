package br.edu.puccampinas.foodflow.ordering.application.port.in;

import br.edu.puccampinas.foodflow.ordering.domain.Order;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Consultas de leitura de pedidos. */
public interface OrderQuery {

    Order getOrder(UUID id);

    Page<Order> listOrders(Pageable pageable);
}
