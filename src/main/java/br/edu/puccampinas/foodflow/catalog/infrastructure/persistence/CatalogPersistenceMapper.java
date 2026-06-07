package br.edu.puccampinas.foodflow.catalog.infrastructure.persistence;

import br.edu.puccampinas.foodflow.catalog.domain.MenuItem;
import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/** Traducao entre o modelo de dominio e o modelo de persistencia do catalogo. */
final class CatalogPersistenceMapper {

    private CatalogPersistenceMapper() {
    }

    static RestaurantJpaEntity toJpa(Restaurant restaurant) {
        List<MenuItemJpaEntity> items = restaurant.menu().stream()
                .map(CatalogPersistenceMapper::toJpa)
                .collect(ArrayList::new, List::add, List::addAll);
        return new RestaurantJpaEntity(
                restaurant.id(), restaurant.name(), restaurant.cuisine(), restaurant.isActive(), items);
    }

    static MenuItemJpaEntity toJpa(MenuItem item) {
        return new MenuItemJpaEntity(
                item.id(),
                item.name(),
                item.description(),
                item.price().amount(),
                item.price().currency().getCurrencyCode(),
                item.isAvailable());
    }

    static Restaurant toDomain(RestaurantJpaEntity entity) {
        List<MenuItem> items = entity.menu.stream()
                .map(CatalogPersistenceMapper::toDomain)
                .toList();
        return Restaurant.reconstitute(entity.id, entity.name, entity.cuisine, entity.active, items);
    }

    static MenuItem toDomain(MenuItemJpaEntity entity) {
        Money price = new Money(entity.price, Currency.getInstance(entity.currency));
        return MenuItem.reconstitute(entity.id, entity.name, entity.description, price, entity.available);
    }
}
