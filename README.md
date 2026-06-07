# FoodFlow

Plataforma de delivery (restaurantes, cardápio, pedidos, pagamento e entrega)
construída como **monólito modular** em Java 25 / Spring Boot 4.

Projeto desenvolvido para o Trabalho Final da disciplina **12452 — Padrões e
Arquitetura de Software** (Escola Politécnica, Engenharia de Software,
PUC-Campinas).

## Como rodar

Pré-requisitos: JDK 25 instalado. O Maven vem embutido no wrapper (`./mvnw`).

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080` usando um banco H2 em memória.
O console do H2 fica em `http://localhost:8080/h2-console`.

Para rodar os testes:

```bash
./mvnw test
```

## Documentação

A documentação arquitetural completa está em construção e ficará organizada em:

- `adrs/` — registros de decisão arquitetural (ADRs)
- `diagrams/` — diagramas (C4, classes, sequência) como código
- `api/` — especificação OpenAPI 3.x da API REST
- `docs/` — documento final do trabalho
