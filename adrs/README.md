# Registros de Decisão Arquitetural (ADRs)

Esta pasta reúne as decisões arquiteturais relevantes do FoodFlow, no formato
proposto por Michael Nygard. Cada ADR registra o contexto que forçou a decisão,
a decisão em si e as consequências (benefícios e custos) esperadas.

| ADR | Título | Status |
|-----|--------|--------|
| [ADR-001](ADR-001-monolito-modular.md) | Adotar monólito modular em vez de microsserviços | Accepted |
| [ADR-002](ADR-002-clean-architecture-por-modulo.md) | Organizar cada módulo em Clean Architecture / Hexagonal | Accepted |
| [ADR-003](ADR-003-postgresql-h2.md) | PostgreSQL como banco principal, H2 em dev e testes | Accepted |
| [ADR-004](ADR-004-status-pedido-enum-switch.md) | Status do pedido como enum + switch | Superseded por ADR-005 |
| [ADR-005](ADR-005-state-pattern-pedido.md) | Adotar o padrão State para o ciclo de vida do pedido | Accepted |
| [ADR-006](ADR-006-eventos-de-dominio.md) | Comunicar mudanças entre módulos por eventos de domínio | Accepted |
| [ADR-007](ADR-007-rest-openapi-versionamento.md) | REST + OpenAPI com versionamento por URL | Accepted |

A sequência ADR-004 → ADR-005 documenta uma decisão **revertida** ao longo do
desenvolvimento: a modelagem inicial do status do pedido com `enum` + `switch`
foi substituída pelo padrão State.
