package br.edu.puccampinas.foodflow.ordering.web;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record ItemRequest(
        @NotNull UUID menuItemId,
        @Positive int quantity) {
}
