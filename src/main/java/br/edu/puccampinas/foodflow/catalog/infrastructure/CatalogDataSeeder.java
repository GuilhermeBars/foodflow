package br.edu.puccampinas.foodflow.catalog.infrastructure;

import br.edu.puccampinas.foodflow.catalog.application.port.in.AddMenuItemUseCase;
import br.edu.puccampinas.foodflow.catalog.application.port.in.AddMenuItemUseCase.AddMenuItemCommand;
import br.edu.puccampinas.foodflow.catalog.application.port.in.RegisterRestaurantUseCase;
import br.edu.puccampinas.foodflow.catalog.application.port.in.RegisterRestaurantUseCase.RegisterRestaurantCommand;
import br.edu.puccampinas.foodflow.catalog.application.port.out.RestaurantRepository;
import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Popula o catalogo com alguns restaurantes na primeira execucao (ambiente de demonstracao). */
@Component
class CatalogDataSeeder implements CommandLineRunner {

    private final RegisterRestaurantUseCase registerRestaurant;
    private final AddMenuItemUseCase addMenuItem;
    private final RestaurantRepository repository;

    CatalogDataSeeder(RegisterRestaurantUseCase registerRestaurant,
                      AddMenuItemUseCase addMenuItem,
                      RestaurantRepository repository) {
        this.registerRestaurant = registerRestaurant;
        this.addMenuItem = addMenuItem;
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (!repository.isEmpty()) {
            return;
        }
        Restaurant cantina = registerRestaurant.register(
                new RegisterRestaurantCommand("Cantina da Nonna", "Italiana"));
        addMenuItem.addItem(new AddMenuItemCommand(
                cantina.id(), "Pizza Margherita", "Molho, mucarela e manjericao", new BigDecimal("49.90")));
        addMenuItem.addItem(new AddMenuItemCommand(
                cantina.id(), "Lasanha Bolonhesa", "Massa fresca ao molho bolonhesa", new BigDecimal("38.00")));

        Restaurant sushi = registerRestaurant.register(
                new RegisterRestaurantCommand("Sushi do Porto", "Japonesa"));
        addMenuItem.addItem(new AddMenuItemCommand(
                sushi.id(), "Combinado 20 pecas", "Selecao do chef", new BigDecimal("72.50")));
        addMenuItem.addItem(new AddMenuItemCommand(
                sushi.id(), "Temaki Salmao", "Cone de alga com salmao fresco", new BigDecimal("27.00")));
    }
}
