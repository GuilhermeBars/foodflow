# ADR-002: Organizar cada módulo em Clean Architecture / Hexagonal

- **Status:** Accepted (2026-05-22)

## Contexto

As regras de negócio mais valiosas do FoodFlow — o ciclo de vida do pedido e o
cálculo de preços — não podem ficar reféns de framework ou de banco. Queremos
poder testar essas regras sem subir o Spring, e trocar detalhes de
infraestrutura (de H2 para PostgreSQL, ou de um gateway de pagamento para outro)
sem mexer no núcleo. A manutenibilidade é um atributo prioritário e depende
diretamente dessa separação.

## Decisão

Organizar **cada módulo** em camadas com a dependência sempre apontando para
dentro (Regra da Dependência / DIP):

- `domain` — modelo de domínio puro (entidades, objetos de valor, eventos,
  exceções de negócio). Sem JPA, sem Spring.
- `application` — portas de entrada (`port.in`, casos de uso) e de saída
  (`port.out`, repositórios e gateways), mais os serviços que as orquestram.
- `infrastructure` — adaptadores: entidades JPA, mapeadores e integrações
  externas que implementam as portas de saída.
- `web` — controladores REST e DTOs (adaptadores de entrada).

O domínio não conhece a infraestrutura; a infraestrutura depende do domínio.

## Consequências

**Benefícios:**
- Domínio testável com testes unitários rápidos, sem contexto Spring.
- Trocar um adaptador (banco, gateway) não toca no domínio nem na aplicação.
- Fronteiras explícitas reforçam a modularidade definida na ADR-001.

**Custos:**
- Mais classes: mapeadores entre o modelo de domínio e as entidades JPA.
- Curva inicial e uma aparente duplicação entre `Restaurant`/`Order` e suas
  entidades de persistência — aceita conscientemente em troca do isolamento.
