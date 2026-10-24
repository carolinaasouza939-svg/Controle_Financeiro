package controlefinanceiro;

import java.time.LocalDate;
import java.util.List;

import controlefinanceiro.controller.GerenciadorFinancas;
import controlefinanceiro.util.ArquivoTransacoes;

public class Main {

    public static void main(String[] args) {

        GerenciadorFinancas gerenciador = new GerenciadorFinancas();

        try {
            gerenciador.adicionarReceita(2500, "Salário", LocalDate.of(2025, 1, 5), "Trabalho");
            gerenciador.adicionarReceita(300, "Freelance", LocalDate.of(2025, 1, 10), "Extra");

            gerenciador.adicionarDespesa(500, "Aluguel", LocalDate.of(2025, 1, 2), "Moradia");
            gerenciador.adicionarDespesa(200, "Mercado", LocalDate.of(2025, 1, 8), "Alimentação");

            System.out.println("Saldo atual: " + gerenciador.calcularSaldo());

            List<Transacao> janeiro = gerenciador.filtrarPorMes(1, 2025);
            System.out.println("Transações de Janeiro:");
            for (Transacao t : janeiro) {
                System.out.println(t.getTipo() + " - " + t.getDescricao() + " - " + t.getValor());
            }

            ArquivoTransacoes arquivo = new ArquivoTransacoes("transacoes.txt");
            arquivo.salvar(gerenciador.getTransacoes());

            List<Transacao> carregadas = arquivo.carregar();
            System.out.println("Transações carregadas do arquivo:");
            for (Transacao t : carregadas) {
                System.out.println(t.getTipo() + " - " + t.getDescricao() + " - " + t.getValor());
            }

            System.out.println("Total de transações: " + Transacao.getTotalTransacoes());

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
