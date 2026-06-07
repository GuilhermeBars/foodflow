package br.edu.puccampinas.foodflow.ordering.web;

import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;

public record PlaceOrderRequest(
        @NotNull UUID restaurantId,
        @NotBlank String customerId,
        @NotEmpty @Valid List<ItemRequest> items,
        @NotNull PaymentMethod paymentMethod,
        @PositiveOrZero double distanceKm,
        String coupon) {
}
