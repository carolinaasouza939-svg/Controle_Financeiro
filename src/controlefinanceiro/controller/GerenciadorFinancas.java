package controlefinanceiro.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controlefinanceiro.Despesa;
import controlefinanceiro.Receita;
import controlefinanceiro.Transacao;

public class GerenciadorFinancas {

    private List<Transacao> transacoes;

    public GerenciadorFinancas() {
        transacoes = new ArrayList<>();
    }

    public void adicionarReceita(double valor, String descricao) {
        if (valor <= 0) {
            throw new IllegalArgumentException();
        }
        transacoes.add(new Receita(valor, descricao));
    }

    public void adicionarReceita(double valor, String descricao, LocalDate data, String categoria) {
        if (valor <= 0) {
            throw new IllegalArgumentException();
        }
        transacoes.add(new Receita(valor, descricao, data, categoria));
    }

    public void adicionarDespesa(double valor, String descricao) {
        if (valor <= 0) {
            throw new IllegalArgumentException();
        }
        transacoes.add(new Despesa(valor, descricao));
    }

    public void adicionarDespesa(double valor, String descricao, LocalDate data, String categoria) {
        if (valor <= 0) {
            throw new IllegalArgumentException();
        }
        transacoes.add(new Despesa(valor, descricao, data, categoria));
    }

    public double calcularSaldo() {
        double saldo = 0;

        for (Transacao t : transacoes) {
            if (t.getTipo().equals("Receita")) {
                saldo += t.getValor();
            } else {
                saldo -= t.getValor();
            }
        }

        return saldo;
    }

    public List<Transacao> filtrarPorCategoria(String categoria) {
        List<Transacao> resultado = new ArrayList<>();

        for (Transacao t : transacoes) {
            if (t.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(t);
            }
        }

        return resultado;
    }

    public List<Transacao> filtrarPorMes(int mes, int ano) {
        List<Transacao> resultado = new ArrayList<>();

        for (Transacao t : transacoes) {
            if (t.getData().getMonthValue() == mes &&
                t.getData().getYear() == ano) {
                resultado.add(t);
            }
        }

        return resultado;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
}
