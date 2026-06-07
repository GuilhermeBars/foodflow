package br.edu.puccampinas.foodflow.ordering.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.edu.puccampinas.foodflow.ordering.application.port.in.AdvanceOrderUseCase;
import br.edu.puccampinas.foodflow.ordering.application.port.in.OrderQuery;
import br.edu.puccampinas.foodflow.ordering.application.port.in.PlaceOrderUseCase;
import br.edu.puccampinas.foodflow.ordering.domain.Order;
import br.edu.puccampinas.foodflow.ordering.domain.OrderItem;
import br.edu.puccampinas.foodflow.shared.domain.Money;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlaceOrderUseCase placeOrder;

    @MockitoBean
    private AdvanceOrderUseCase advanceOrder;

    @MockitoBean
    private OrderQuery orderQuery;

    @Test
    void criaPedidoRetorna201() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        Order order = Order.place(restaurantId, "cliente-1",
                List.of(new OrderItem(menuItemId, "Pizza", Money.brl("49.90"), 2)));
        given(placeOrder.place(any())).willReturn(order);

        String body = """
                {"restaurantId":"%s","customerId":"cliente-1","items":[{"menuItemId":"%s","quantity":2}],
                 "paymentMethod":"PIX","distanceKm":3.0}
                """.formatted(restaurantId, menuItemId);

        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.customerId").value("cliente-1"))
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    void payloadInvalidoRetorna400ComProblemDetail() throws Exception {
        String body = """
                {"restaurantId":"%s","customerId":"","items":[],"paymentMethod":"PIX","distanceKm":1.0}
                """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Requisicao invalida"))
                .andExpect(jsonPath("$.errors.items").exists());
    }
}
