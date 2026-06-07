package br.edu.puccampinas.foodflow.catalog.application;

import br.edu.puccampinas.foodflow.catalog.application.port.in.AddMenuItemUseCase;
import br.edu.puccampinas.foodflow.catalog.application.port.in.CatalogQuery;
import br.edu.puccampinas.foodflow.catalog.application.port.in.RegisterRestaurantUseCase;
import br.edu.puccampinas.foodflow.catalog.application.port.out.RestaurantRepository;
import br.edu.puccampinas.foodflow.catalog.domain.MenuItem;
import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import br.edu.puccampinas.foodflow.shared.domain.EntityNotFoundException;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orquestra os casos de uso do catalogo. Implementa varias portas de entrada
 * pequenas (ISP) e depende apenas da porta de saida {@link RestaurantRepository}.
 */
@Service
public class CatalogService implements RegisterRestaurantUseCase, AddMenuItemUseCase, CatalogQuery {

    private final RestaurantRepository repository;

    public CatalogService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Restaurant register(RegisterRestaurantCommand command) {
        return repository.save(Restaurant.create(command.name(), command.cuisine()));
    }

    @Override
    @Transactional
    public MenuItem addItem(AddMenuItemCommand command) {
        Restaurant restaurant = getRestaurant(command.restaurantId());
        MenuItem item = restaurant.addMenuItem(command.name(), command.description(), Money.brl(command.price()));
        repository.save(restaurant);
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurant getRestaurant(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante %s nao encontrado".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> listRestaurants() {
        return repository.findAll();
    }
}
