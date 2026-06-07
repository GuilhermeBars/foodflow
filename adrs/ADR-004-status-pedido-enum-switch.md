# ADR-004: Representar o status do pedido como enum com validação por switch

- **Status:** Superseded por [ADR-005](ADR-005-state-pattern-pedido.md) (2026-05-27)

## Contexto

Na primeira modelagem do ciclo de vida do pedido, buscávamos a solução mais
direta. Um pedido passa por vários estados (criado, confirmado, em preparo, saiu
para entrega, entregue, cancelado), e era preciso impedir transições inválidas
(por exemplo, entregar um pedido que ainda não saiu para entrega).

## Decisão (à época)

Representar o estado com um `enum OrderStatus` e validar as transições com
blocos `switch`/`if` dentro do serviço de pedidos, checando o status atual antes
de cada mudança.

## Consequências

O que se observou na prática levou à reversão desta decisão:

- A lógica de transição **se espalhou**: cada operação (confirmar, despachar,
  entregar, cancelar) repetia um `switch` sobre o status, com baixa coesão.
- **Fácil esquecer um caso** e deixar passar uma transição inválida.
- **Difícil de estender**: incluir um novo status obrigava revisar todos os
  `switch` existentes — uma violação clara do princípio Aberto/Fechado.

Por esses motivos a decisão foi **substituída pela ADR-005**, que adota o padrão
State. Este registro é mantido para preservar o histórico do raciocínio.
