# ADR-001: Adotar monólito modular em vez de microsserviços

- **Status:** Accepted (2026-05-21)

## Contexto

O FoodFlow é desenvolvido por uma equipe de estudantes, em prazo curto e sem
infraestrutura de orquestração (Kubernetes, malha de serviços, observabilidade
distribuída). O domínio tem fronteiras razoavelmente claras — catálogo, pedidos,
precificação, pagamento e notificação —, mas o fluxo central (criar um pedido)
atravessa vários desses contextos numa única operação que precisa ser
consistente.

Os atributos de qualidade prioritários são manutenibilidade e confiabilidade, e
não escala massiva. Distribuir o sistema em microsserviços traria, neste
momento, mais custo (deploys independentes, comunicação por rede, consistência
eventual, depuração distribuída) do que benefício.

## Decisão

Empacotar o sistema como **um único artefato implantável (monólito)**, porém
**modular**: cada contexto é um módulo com fronteira explícita (um pacote de
primeiro nível por módulo) e a comunicação entre módulos passa por portas
(interfaces) ou por eventos de domínio, nunca por acesso direto a tabelas ou
classes internas de outro módulo.

## Consequências

**Benefícios:**
- Build, deploy e teste simples; uma única aplicação para subir.
- Transações locais ACID no fluxo de pedido (sem saga/consistência eventual).
- Refatorar uma fronteira entre módulos é uma mudança de código, não de contrato
  de rede.
- Onboarding rápido para a equipe.

**Custos:**
- Escala apenas por replicação do conjunto inteiro, não por serviço.
- Exige disciplina para não vazar fronteiras entre módulos (mitigado por portas
  e revisão de código).
- Uma falha grave pode afetar a aplicação toda (mitigado por testes e por manter
  os módulos coesos).

Caminho de evolução: se um módulo passar a ter requisitos próprios de escala,
ele já está isolado por portas e pode ser extraído para um serviço.
