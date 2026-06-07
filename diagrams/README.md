# Diagramas

Diagramas do FoodFlow como código (Mermaid), versionados junto ao projeto. Cada
arquivo `.mmd` pode ser renderizado para imagem com:

```bash
npx -y @mermaid-js/mermaid-cli -i <arquivo>.mmd -o <arquivo>.png -t neutral -b white
```

| Arquivo | O que mostra |
|---------|--------------|
| [c4-context.mmd](c4-context.mmd) | C4 nível 1 — contexto: atores e sistemas externos |
| [c4-container.mmd](c4-container.mmd) | C4 nível 2 — containers/módulos do monólito e o banco |
| [class-state.mmd](class-state.mmd) | Diagrama de classes do padrão **State** (ciclo do pedido) |
| [class-strategy-pricing.mmd](class-strategy-pricing.mmd) | Diagrama de classes do padrão **Strategy** (frete e desconto) |
| [class-factory-payment.mmd](class-factory-payment.mmd) | Diagrama de classes dos padrões **Adapter + Factory** (pagamento) |
| [sequence-place-order.mmd](sequence-place-order.mmd) | Diagrama de sequência do fluxo central "criar pedido" |

Os diagramas refletem o código implementado nos módulos `ordering`, `pricing`,
`payment` e `notification`.
