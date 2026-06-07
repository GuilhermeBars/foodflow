package br.edu.puccampinas.foodflow.ordering.web;

import br.edu.puccampinas.foodflow.ordering.application.port.in.AdvanceOrderUseCase;
import br.edu.puccampinas.foodflow.ordering.application.port.in.OrderQuery;
import br.edu.puccampinas.foodflow.ordering.application.port.in.PlaceOrderUseCase;
import br.edu.puccampinas.foodflow.ordering.application.port.in.PlaceOrderUseCase.PlaceOrderCommand;
import br.edu.puccampinas.foodflow.ordering.application.port.in.PlaceOrderUseCase.PlaceOrderCommand.Line;
import br.edu.puccampinas.foodflow.ordering.domain.Order;
import br.edu.puccampinas.foodflow.shared.web.PageResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** API REST de pedidos. Depende apenas dos casos de uso (portas de entrada). */
@RestController
@RequestMapping("/api/v1/orders")
class OrderController {

    private final PlaceOrderUseCase placeOrder;
    private final AdvanceOrderUseCase advanceOrder;
    private final OrderQuery orderQuery;

    OrderController(PlaceOrderUseCase placeOrder, AdvanceOrderUseCase advanceOrder, OrderQuery orderQuery) {
        this.placeOrder = placeOrder;
        this.advanceOrder = advanceOrder;
        this.orderQuery = orderQuery;
    }

    @PostMapping
    ResponseEntity<OrderResponse> place(@Valid @RequestBody PlaceOrderRequest request) {
        List<Line> lines = request.items().stream()
                .map(item -> new Line(item.menuItemId(), item.quantity()))
                .toList();
        Order order = placeOrder.place(new PlaceOrderCommand(
                request.restaurantId(),
                request.customerId(),
                lines,
                request.paymentMethod(),
                request.distanceKm(),
                request.coupon()));
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }

    @GetMapping
    PageResponse<OrderResponse> list(@PageableDefault(size = 20) Pageable pageable) {
        return PageResponse.from(orderQuery.listOrders(pageable).map(OrderResponse::from));
    }

    @GetMapping("/{id}")
    OrderResponse getById(@PathVariable UUID id) {
        return OrderResponse.from(orderQuery.getOrder(id));
    }

    @PostMapping("/{id}/transitions")
    OrderResponse transition(@PathVariable UUID id, @Valid @RequestBody TransitionRequest request) {
        return OrderResponse.from(advanceOrder.apply(id, request.transition()));
    }
}
