package br.edu.puccampinas.foodflow.ordering.application.port.out;

import br.edu.puccampinas.foodflow.ordering.domain.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Porta de saida para persistencia de pedidos. */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    Page<Order> findAll(Pageable pageable);
}
