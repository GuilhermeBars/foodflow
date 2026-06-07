# ADR-003: PostgreSQL como banco principal, H2 em desenvolvimento e testes

- **Status:** Accepted (2026-05-23)

## Contexto

Pedidos e pagamentos exigem persistência relacional com integridade referencial
e bom comportamento sob concorrência. Em produção, queremos durabilidade e um
banco maduro. Em desenvolvimento e na integração contínua, queremos rapidez e
zero configuração — instalar um banco em cada máquina atrasaria a equipe.

## Decisão

Usar **PostgreSQL** como banco em produção (perfil `prod`, parametrizado por
variáveis de ambiente) e **H2 em memória** como padrão de desenvolvimento e nos
testes automatizados. O acesso é feito por JPA/Hibernate, que abstrai o dialeto.
Em produção `ddl-auto` fica em `validate`; em desenvolvimento, `create-drop` com
dados de exemplo (seed).

## Consequências

**Benefícios:**
- `./mvnw spring-boot:run` e `./mvnw test` funcionam sem instalar banco algum.
- Produção apoiada em um banco robusto e amplamente conhecido.
- O código de persistência é o mesmo nos dois ambientes (apenas configuração
  muda).

**Custos:**
- Risco de diferenças sutis de comportamento entre H2 e PostgreSQL (mitigado por
  usar recursos padrão de JPA e evitar SQL específico de fornecedor).
- Para produção real será necessário adotar migrações versionadas (Flyway ou
  Liquibase) em vez de `ddl-auto` — registrado como trabalho futuro.
