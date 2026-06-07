# ADR-006: Comunicar mudanças entre módulos por eventos de domínio

- **Status:** Accepted (2026-05-29)

## Contexto

Quando um pedido muda de status, vários interessados precisam reagir: avisar o
cliente, avisar a cozinha do restaurante e registrar métricas. O módulo de
pedidos não deveria conhecer esses notificadores — isso o acoplaria ao módulo de
notificação e dificultaria adicionar novos canais (e-mail, push, SMS) no futuro.

## Decisão

Usar **eventos de domínio** com o padrão **Observer**:

- O agregado registra eventos (`OrderStatusChangedEvent`) ao mudar de estado.
- Após persistir o pedido, um `DomainEventPublisher` (no módulo `shared`) publica
  esses eventos através do `ApplicationEventPublisher` do Spring.
- Os observadores (`CustomerNotifier`, `RestaurantNotifier`, `AnalyticsListener`)
  reagem com `@EventListener`, cada um decidindo a quais transições responde.

## Consequências

**Benefícios:**
- O módulo de pedidos não conhece nenhum observador — baixo acoplamento.
- Adicionar um novo canal de notificação é criar um novo listener, sem tocar em
  pedidos (OCP) — reforça as fronteiras modulares da ADR-001.

**Custos:**
- O fluxo fica menos explícito (a reação acontece "à distância", via evento).
- Como os eventos são publicados após a persistência, garantias mais fortes
  exigiriam listeners transacionais ou um padrão *outbox* — registrado como
  evolução futura.
