# 💰 Controle Financeiro Pessoal

Uma aplicação desktop desenvolvida em **Java (Swing)** para ajudar os utilizadores a gerir as suas finanças de forma simples e eficiente. O sistema suporta múltiplos utilizadores, controlo detalhado de receitas e despesas, e visualização de dados através de gráficos interativos.

## ✨ Funcionalidades

* **Autenticação e Sessão:** Sistema de login e registo de utilizadores (com senhas guardadas em *hash*) e gestão de sessão ativa.
* **Gestão de Transações:** Adição, categorização e controlo de receitas e despesas.
* **Dashboard Visual:** Geração de gráficos de resumo mensal utilizando a biblioteca **JFreeChart** para facilitar a análise financeira.
* **Isolamento de Dados:** Cada utilizador possui o seu próprio histórico financeiro, garantindo total privacidade.

## 🛠️ Tecnologias e Arquitetura

O projeto segue o padrão de arquitetura **MVC (Model-View-Controller)**:
* **Linguagem:** Java 21
* **Interface Gráfica:** Java Swing
* **Gestão de Dependências:** Maven (`pom.xml`)
* **Bibliotecas Externas:** JFreeChart (para a geração de gráficos)

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java Development Kit (JDK) 21** instalado no sistema.
* Um IDE compatível com Maven (as instruções abaixo focam-se no **IntelliJ IDEA**).

### Passos para execução
1. Abra o IDE, vá a `File > Open` e selecione a pasta raiz do projeto (que contém o ficheiro `pom.xml`).
2. Clique em **"Trust Project"** e aguarde que o Maven importe o projeto e descarregue as dependências necessárias.
3. Configure a versão do Java acedendo a `File > Project Structure > Project SDK` e selecionando o **JDK 21**.
4. No explorador lateral, navegue até `src/main/java/controlefinanceiro/Main.java`.
5. Clique com o botão direito no ficheiro `Main.java` e selecione **Run 'Main.main()'**.

## 🗄️ Armazenamento de Dados

A aplicação utiliza persistência de dados local baseada em ficheiros de texto. Os dados são criados dinamicamente na pasta do projeto à medida que o sistema é utilizado:

* `usuarios.txt`: Guarda as credenciais dos utilizadores registados.
* `usuarios/transacoes_NOME.txt`: Ficheiros individuais criados automaticamente para registar o histórico de cada utilizador.

> **Nota de Segurança:** Por conterem informações sensíveis, os ficheiros de dados locais e os ficheiros de configuração da IDE estão mapeados no `.gitignore` e não devem ser enviados para o repositório Git.

## 📂 Estrutura do Projeto

O código fonte está organizado da seguinte forma para separar responsabilidades de forma clara:

```text
src/main/
├── java/controlefinanceiro/
│   ├── Main.java                 # Classe principal (Ponto de entrada)
│   ├── model/                    # Modelos e regras de negócio
│   │   ├── Transacao, Receita, Despesa
│   │   └── ResumoFinanceiro
│   ├── controller/               # Controladores (intermediação lógica)
│   │   └── GerenciadorFinancas
│   ├── util/                     # Persistência de dados e utilitários
│   │   ├── ArquivoTransacoes
│   │   ├── RepositorioUsuarios
│   │   └── SessaoUsuario
│   └── view/                     # Ecrãs e componentes visuais (Interfaces)
│       ├── Login, Cadastro
│       └── AppFinanceiro, TelaGrafico
└── resources/img/
    └── bntvolta.png              # Ativos visuais e imagens da interface
