package br.edu.puccampinas.foodflow.ordering.web;

import br.edu.puccampinas.foodflow.ordering.application.port.in.OrderTransition;
import jakarta.validation.constraints.NotNull;

public record TransitionRequest(@NotNull OrderTransition transition) {
}
