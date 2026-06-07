# FoodFlow

FoodFlow é uma plataforma de delivery: restaurantes publicam seu cardápio,
clientes montam um pedido, o sistema calcula o total, cobra o pagamento e
acompanha o pedido até a entrega. O código foi escrito para o Trabalho Final da
disciplina 12452 (Padrões e Arquitetura de Software) da PUC-Campinas, com foco em
mostrar arquitetura, SOLID, Clean Code e padrões GoF aplicados de forma
justificada.

Repositório: https://github.com/GuilhermeBars/foodflow

## Integrantes

Disciplina 12452 (Padrões e Arquitetura de Software), Curso de Engenharia de
Software da PUC-Campinas. Professor: Prof. Dr. Douglas Henrique Siqueira Abreu.

| Integrante | RA |
|------------|-----|
| Felipe Cosmo Granziol | 24021602 |
| Guilherme Bars | 24014122 |
| Gustavo Kurten | 24008150 |
| Henrique Gambin | 24013762 |
| João Celso | 24012463 |
| Pedro Tiezo Sales Shimizu | 24005158 |
| Vitor Hugo Barbosa | 24018852 |

## O problema

Um pedido de delivery atravessa várias responsabilidades que costumam virar um
emaranhado de `if`s num único serviço: validar o cardápio, somar itens, calcular
frete e desconto, cobrar, controlar o estado do pedido e avisar os interessados.
Quando tudo isso fica junto, qualquer mudança (uma nova forma de pagamento, uma
nova regra de frete, um novo estado) obriga a mexer no mesmo lugar e arrisca
quebrar o resto.

O FoodFlow separa essas responsabilidades em módulos com fronteiras claras e usa
padrões de projeto onde eles resolvem um problema concreto, não por enfeite.

O público-alvo são clientes (que fazem pedidos) e restaurantes (que gerenciam o
cardápio e acompanham os pedidos). A API é o ponto de integração para um
aplicativo ou site que consuma esses dados.

## Arquitetura

No plano macro, o sistema é um monólito modular: um único artefato para subir,
dividido em módulos com fronteiras explícitas. A escolha está registrada na
[ADR-001](adrs/ADR-001-monolito-modular.md).

No plano interno, cada módulo segue Clean Architecture / Hexagonal
([ADR-002](adrs/ADR-002-clean-architecture-por-modulo.md)):

- `domain`: modelo de domínio puro, sem Spring nem JPA.
- `application`: portas de entrada (`port.in`) e de saída (`port.out`) e os
  serviços que as orquestram.
- `infrastructure`: adaptadores (JPA, integrações externas) que implementam as
  portas de saída.
- `web`: controladores REST e DTOs.

A dependência sempre aponta para dentro: a infraestrutura conhece o domínio, e
não o contrário.

Módulos:

| Módulo | Responsabilidade |
|--------|------------------|
| `shared` | Tipos comuns: `Money`, eventos de domínio, exceções |
| `catalog` | Restaurantes e itens de cardápio |
| `ordering` | Pedidos e seu ciclo de vida |
| `pricing` | Frete e desconto |
| `payment` | Cobrança e idempotência |
| `notification` | Reações a mudanças do pedido |

## Atributos de qualidade priorizados

À luz da ISO/IEC 25010, três atributos guiaram as decisões:

- Manutenibilidade: fronteiras de módulo, domínio sem framework e inversão de
  dependência permitem mudar uma parte sem arrastar as outras.
- Confiabilidade: o estado do pedido é protegido por uma máquina de estados que
  rejeita transições inválidas, e o pagamento é idempotente.
- Eficiência de desempenho: as listagens são paginadas e as notificações saem do
  caminho da requisição via eventos.

## Como rodar

Pré-requisito: JDK 25. O Maven vem no wrapper (`./mvnw`), não precisa instalar
nada além do JDK.

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080` com um banco H2 em memória, já
populado com dois restaurantes de exemplo. O console do H2 fica em
`http://localhost:8080/h2-console`.

Para rodar os testes:

```bash
./mvnw test
```

Em produção o perfil `prod` usa PostgreSQL, configurado por variáveis de
ambiente (`FOODFLOW_DB_URL`, `FOODFLOW_DB_USER`, `FOODFLOW_DB_PASSWORD`). Veja a
[ADR-003](adrs/ADR-003-postgresql-h2.md).

## API

A API é REST, versionada por URL (`/api/v1`), com erros em Problem Details
(RFC 7807) e listagens paginadas. O contrato completo está em
[api/openapi.yaml](api/openapi.yaml).

Listar os restaurantes (e descobrir os ids do seed):

```bash
curl http://localhost:8080/api/v1/restaurants
```

Criar um restaurante:

```bash
curl -X POST http://localhost:8080/api/v1/restaurants \
  -H 'Content-Type: application/json' \
  -d '{"name":"Burger House","cuisine":"Hamburgueria"}'
```

Criar um pedido (troque os ids pelos que vierem da listagem):

```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H 'Content-Type: application/json' \
  -d '{
    "restaurantId":"<id-do-restaurante>",
    "customerId":"cliente-42",
    "items":[{"menuItemId":"<id-do-item>","quantity":2}],
    "paymentMethod":"PIX",
    "distanceKm":3.5,
    "coupon":"BEMVINDO"
  }'
```

Avançar o pedido (confirmar, preparar, despachar, entregar ou cancelar):

```bash
curl -X POST http://localhost:8080/api/v1/orders/<id-do-pedido>/transitions \
  -H 'Content-Type: application/json' \
  -d '{"transition":"CONFIRM"}'
```

Principais endpoints:

| Método | Caminho | O que faz |
|--------|---------|-----------|
| GET | `/api/v1/restaurants` | Lista restaurantes |
| POST | `/api/v1/restaurants` | Cadastra restaurante |
| POST | `/api/v1/restaurants/{id}/menu-items` | Adiciona item ao cardápio |
| POST | `/api/v1/orders` | Cria pedido |
| GET | `/api/v1/orders` | Lista pedidos (paginado) |
| GET | `/api/v1/orders/{id}` | Consulta um pedido |
| POST | `/api/v1/orders/{id}/transitions` | Avança ou cancela o pedido |

## Padrões de projeto aplicados

Cada padrão entrou para resolver um problema do próprio sistema:

- State: o ciclo de vida do pedido. Cada estado conhece suas transições válidas e
  o `Order` delega a ele, sem `switch` sobre o status. Ver
  [ADR-005](adrs/ADR-005-state-pattern-pedido.md).
- Strategy: as políticas de frete e desconto, intercambiáveis sem alterar o
  cálculo do total.
- Adapter: cada gateway de pagamento (Pix, cartão) atrás de uma porta comum.
- Factory: a escolha do gateway conforme a forma de pagamento.
- Observer: os notificadores reagem aos eventos do pedido sem que o módulo de
  pedidos os conheça.

## Princípios SOLID

A organização em portas e adaptadores carrega os cinco princípios: serviços com
uma responsabilidade, estratégias que estendem comportamento sem modificar o
existente (Aberto/Fechado), implementações de estratégia substituíveis entre si
(Liskov), portas de entrada pequenas e específicas (Segregação de Interface) e
dependência sempre sobre abstrações (Inversão de Dependência). O documento final
detalha cada um com trechos de código.

## Estrutura do repositório

```
src/                 código-fonte (módulos shared, catalog, ordering, pricing, payment, notification)
adrs/                registros de decisão arquitetural (ADRs)
diagrams/            diagramas C4, de classes e de sequência (Mermaid)
api/                 especificação OpenAPI 3.x
docs/                documento final do trabalho
```

## Testes

São 29 testes automatizados cobrindo a máquina de estados do pedido, as
estratégias de preço, a fábrica e a idempotência de pagamento, o caso de uso de
criação de pedido e a camada web. Rode com `./mvnw test`.
