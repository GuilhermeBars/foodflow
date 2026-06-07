package br.edu.puccampinas.foodflow.payment.application;

import br.edu.puccampinas.foodflow.payment.application.port.out.PaymentGateway;
import br.edu.puccampinas.foodflow.payment.domain.PaymentMethod;
import br.edu.puccampinas.foodflow.shared.domain.BusinessRuleException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Fabrica que devolve o {@link PaymentGateway} adequado a cada forma de
 * pagamento. Recebe todos os gateways disponiveis e os indexa pelo metodo que
 * suportam; registrar um novo provedor e apenas adicionar um bean (OCP).
 */
@Component
public class PaymentGatewayFactory {

    private final Map<PaymentMethod, PaymentGateway> gatewaysByMethod;

    public PaymentGatewayFactory(List<PaymentGateway> gateways) {
        this.gatewaysByMethod = new EnumMap<>(PaymentMethod.class);
        for (PaymentGateway gateway : gateways) {
            this.gatewaysByMethod.put(gateway.supportedMethod(), gateway);
        }
    }

    public PaymentGateway forMethod(PaymentMethod method) {
        PaymentGateway gateway = gatewaysByMethod.get(method);
        if (gateway == null) {
            throw new BusinessRuleException("Forma de pagamento nao suportada: " + method);
        }
        return gateway;
    }
}
