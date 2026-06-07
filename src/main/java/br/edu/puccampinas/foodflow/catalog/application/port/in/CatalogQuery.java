package br.edu.puccampinas.foodflow.catalog.application.port.in;

import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import java.util.List;
import java.util.UUID;

/** Consultas de leitura do catalogo, usadas pela web e por outros modulos. */
public interface CatalogQuery {

    Restaurant getRestaurant(UUID id);

    List<Restaurant> listRestaurants();
}
