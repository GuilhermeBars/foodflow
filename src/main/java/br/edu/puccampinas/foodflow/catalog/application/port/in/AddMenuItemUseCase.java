package br.edu.puccampinas.foodflow.catalog.application.port.in;

import br.edu.puccampinas.foodflow.catalog.domain.MenuItem;
import java.math.BigDecimal;
import java.util.UUID;

/** Caso de uso: adicionar um item ao cardapio de um restaurante. */
public interface AddMenuItemUseCase {

    MenuItem addItem(AddMenuItemCommand command);

    record AddMenuItemCommand(UUID restaurantId, String name, String description, BigDecimal price) {
    }
}
