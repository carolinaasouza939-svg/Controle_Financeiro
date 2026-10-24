# Controle Financeiro Pessoal

Aplicação Java (Swing) com login/cadastro, controle de receitas e despesas por usuário e gráfico de resumo mensal (JFreeChart).

## Como executar (IntelliJ IDEA)
1. `File > Open` e selecione a pasta que contém o `pom.xml` (aceite "Trust Project" e deixe o Maven importar).
2. Use o JDK 21 (`File > Project Structure > Project SDK`).
3. Rode `src/main/java/controlefinanceiro/Main.java`.

Os dados são criados na pasta do projeto ao usar o programa: `usuarios.txt` (usuários, com senha em hash) e `usuarios/transacoes_NOME.txt`. Esses arquivos não vão para o Git (veja o `.gitignore`).

## Estrutura
```
src/main/java/controlefinanceiro/
├── Main.java
├── model/        Transacao, Receita, Despesa, ResumoFinanceiro
├── controller/   GerenciadorFinancas
├── util/         ArquivoTransacoes, RepositorioUsuarios, SessaoUsuario
└── view/         Login, Cadastro, AppFinanceiro, TelaGrafico
src/main/resources/img/bntvolta.png
```
