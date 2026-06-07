package br.edu.puccampinas.foodflow.catalog.web;

import br.edu.puccampinas.foodflow.catalog.domain.MenuItem;
import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String currency,
        boolean available) {

    static MenuItemResponse from(MenuItem item) {
        return new MenuItemResponse(
                item.id(),
                item.name(),
                item.description(),
                item.price().amount(),
                item.price().currency().getCurrencyCode(),
                item.isAvailable());
    }
}
