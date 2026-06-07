package br.edu.puccampinas.foodflow.catalog.application.port.in;

import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;

/** Caso de uso: cadastrar um novo restaurante. */
public interface RegisterRestaurantUseCase {

    Restaurant register(RegisterRestaurantCommand command);

    record RegisterRestaurantCommand(String name, String cuisine) {
    }
}
