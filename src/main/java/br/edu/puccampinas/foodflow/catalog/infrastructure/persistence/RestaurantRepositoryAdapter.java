package br.edu.puccampinas.foodflow.catalog.infrastructure.persistence;

import br.edu.puccampinas.foodflow.catalog.application.port.out.RestaurantRepository;
import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** Adapter que liga a porta de saida do catalogo ao Spring Data JPA. */
@Component
class RestaurantRepositoryAdapter implements RestaurantRepository {

    private final SpringDataRestaurantRepository jpa;

    RestaurantRepositoryAdapter(SpringDataRestaurantRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return CatalogPersistenceMapper.toDomain(jpa.save(CatalogPersistenceMapper.toJpa(restaurant)));
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return jpa.findById(id).map(CatalogPersistenceMapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return jpa.findAll().stream().map(CatalogPersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean isEmpty() {
        return jpa.count() == 0;
    }
}
