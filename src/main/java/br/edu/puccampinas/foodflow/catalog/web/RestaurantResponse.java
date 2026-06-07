package br.edu.puccampinas.foodflow.catalog.web;

import br.edu.puccampinas.foodflow.catalog.domain.Restaurant;
import java.util.List;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String cuisine,
        boolean active,
        List<MenuItemResponse> menu) {

    static RestaurantResponse from(Restaurant restaurant) {
        List<MenuItemResponse> menu = restaurant.menu().stream()
                .map(MenuItemResponse::from)
                .toList();
        return new RestaurantResponse(
                restaurant.id(), restaurant.name(), restaurant.cuisine(), restaurant.isActive(), menu);
    }
}
