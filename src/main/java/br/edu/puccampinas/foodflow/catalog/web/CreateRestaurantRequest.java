package br.edu.puccampinas.foodflow.catalog.web;

import jakarta.validation.constraints.NotBlank;

public record CreateRestaurantRequest(
        @NotBlank String name,
        @NotBlank String cuisine) {
}
