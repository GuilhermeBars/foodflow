package br.edu.puccampinas.foodflow.ordering.application.port.in;

import br.edu.puccampinas.foodflow.ordering.domain.Order;
import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import java.util.List;
import java.util.UUID;

/** Caso de uso: registrar um novo pedido (com precificacao e cobranca). */
public interface PlaceOrderUseCase {

    Order place(PlaceOrderCommand command);

    record PlaceOrderCommand(
            UUID restaurantId,
            String customerId,
            List<Line> items,
            PaymentMethod paymentMethod,
            double distanceKm,
            String coupon) {

        public record Line(UUID menuItemId, int quantity) {
        }
    }
}
