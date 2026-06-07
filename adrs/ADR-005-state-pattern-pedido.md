# ADR-005: Adotar o padrão State para o ciclo de vida do pedido

- **Status:** Accepted (2026-05-27) — Substitui a [ADR-004](ADR-004-status-pedido-enum-switch.md)

## Contexto

A abordagem de `enum` + `switch` (ADR-004) espalhou as regras de transição e se
mostrou frágil e difícil de estender. Precisávamos de uma forma em que cada
estado conhecesse apenas as suas transições válidas, sem condicionais espalhadas,
e em que adicionar um estado novo não obrigasse a mexer nos existentes.

## Decisão

Aplicar o padrão **State (GoF)**:

- Uma interface `OrderState` com transições padrão (`confirm`, `startPreparing`,
  `dispatch`, `deliver`, `cancel`) que, por omissão, **rejeitam** a operação
  lançando `BusinessRuleException`.
- Uma classe por estado (`CreatedState`, `ConfirmedState`, `PreparingState`,
  `OutForDeliveryState`, `DeliveredState`, `CancelledState`), cada uma
  sobrescrevendo somente as transições válidas a partir dela.
- O agregado `Order` mantém uma referência ao estado atual e **delega** cada
  operação a ele, sem nenhum `if`/`switch` sobre o status.

## Consequências

**Benefícios:**
- Cada estado fica isolado e coeso; as transições válidas ficam evidentes.
- Transições inválidas são barradas por padrão (comportamento seguro).
- Incluir um novo estado é criar uma nova classe, sem tocar nas demais (OCP).
- O domínio do pedido fica livre de condicionais sobre status.

**Custos:**
- Mais classes (uma por estado).
- Reconstruir o estado a partir do status persistido exige um pequeno mapeamento
  (`OrderState.forStatus`).

O ganho de clareza e segurança compensa o número maior de classes.
