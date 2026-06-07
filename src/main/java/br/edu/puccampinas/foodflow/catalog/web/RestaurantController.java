package br.edu.puccampinas.foodflow.catalog.web;

import br.edu.puccampinas.foodflow.catalog.application.port.in.AddMenuItemUseCase;
import br.edu.puccampinas.foodflow.catalog.application.port.in.AddMenuItemUseCase.AddMenuItemCommand;
import br.edu.puccampinas.foodflow.catalog.application.port.in.CatalogQuery;
import br.edu.puccampinas.foodflow.catalog.application.port.in.RegisterRestaurantUseCase;
import br.edu.puccampinas.foodflow.catalog.application.port.in.RegisterRestaurantUseCase.RegisterRestaurantCommand;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST do catalogo. Depende de portas de entrada estreitas (ISP) em vez do
 * servico concreto, e nunca expoe o modelo de dominio diretamente (usa DTOs).
 */
@RestController
@RequestMapping("/api/v1/restaurants")
class RestaurantController {

    private final RegisterRestaurantUseCase registerRestaurant;
    private final AddMenuItemUseCase addMenuItem;
    private final CatalogQuery catalogQuery;

    RestaurantController(RegisterRestaurantUseCase registerRestaurant,
                         AddMenuItemUseCase addMenuItem,
                         CatalogQuery catalogQuery) {
        this.registerRestaurant = registerRestaurant;
        this.addMenuItem = addMenuItem;
        this.catalogQuery = catalogQuery;
    }

    @PostMapping
    ResponseEntity<RestaurantResponse> create(@Valid @RequestBody CreateRestaurantRequest request) {
        var restaurant = registerRestaurant.register(
                new RegisterRestaurantCommand(request.name(), request.cuisine()));
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantResponse.from(restaurant));
    }

    @GetMapping
    List<RestaurantResponse> list() {
        return catalogQuery.listRestaurants().stream().map(RestaurantResponse::from).toList();
    }

    @GetMapping("/{id}")
    RestaurantResponse getById(@PathVariable UUID id) {
        return RestaurantResponse.from(catalogQuery.getRestaurant(id));
    }

    @PostMapping("/{id}/menu-items")
    ResponseEntity<MenuItemResponse> addItem(@PathVariable UUID id,
                                             @Valid @RequestBody CreateMenuItemRequest request) {
        var item = addMenuItem.addItem(
                new AddMenuItemCommand(id, request.name(), request.description(), request.price()));
        return ResponseEntity.status(HttpStatus.CREATED).body(MenuItemResponse.from(item));
    }
}
