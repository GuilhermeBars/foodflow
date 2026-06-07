# ADR-007: Expor a API em REST + OpenAPI com versionamento por URL

- **Status:** Accepted (2026-05-30)

## Contexto

A API do FoodFlow será consumida por clientes externos (aplicativo e web).
Precisamos de um contrato claro e versionável, da capacidade de evoluir sem
quebrar clientes existentes, de erros padronizados e de ferramentas maduras de
documentação e teste.

## Decisão

Expor uma **API REST** orientada a recursos (`/restaurants`, `/orders`), com:

- **Versionamento por URL**: todos os caminhos sob `/api/v1`.
- **Erros em Problem Details (RFC 7807)**: respostas de erro com `status`,
  `title`, `detail` e, na validação, um mapa `errors` por campo.
- **Paginação** explícita nas listagens (envelope `PageResponse`).
- Contrato especificado em **OpenAPI 3.x**, versionado no repositório
  (`/api/openapi.yaml`).

## Consequências

**Benefícios:**
- Contrato explícito, legível por humanos e ferramentas, versionado junto ao
  código.
- Versionamento por URL é simples de rotear, cachear e depurar.
- Erros padronizados facilitam o tratamento no cliente.

**Custos:**
- Versionamento por URL pode levar à duplicação de rotas em mudanças muito
  grandes (aceitável no escopo atual).
- Manter o `openapi.yaml` em sincronia com o código exige disciplina; uma
  geração automática a partir das anotações pode ser adotada no futuro.
