package br.edu.puccampinas.foodflow.catalog.application.port.out;

import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de saida do catalogo. O dominio depende desta abstracao; quem a
 * implementa (JPA, memoria, etc.) e detalhe de infraestrutura (DIP).
 */
public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(UUID id);

    List<Restaurant> findAll();

    boolean isEmpty();
}
