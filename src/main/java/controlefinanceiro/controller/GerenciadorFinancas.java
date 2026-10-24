package controlefinanceiro.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controlefinanceiro.model.Despesa;
import controlefinanceiro.model.Receita;
import controlefinanceiro.model.Transacao;

public class GerenciadorFinancas {

    private List<Transacao> transacoes = new ArrayList<>();

    public void adicionarReceita(double valor, String descricao) {
        transacoes.add(new Receita(valor, descricao));
    }

    public void adicionarReceita(double valor, String descricao, LocalDate data, String categoria) {
        transacoes.add(new Receita(valor, descricao, data, categoria));
    }

    public void adicionarDespesa(double valor, String descricao) {
        transacoes.add(new Despesa(valor, descricao));
    }

    public void adicionarDespesa(double valor, String descricao, LocalDate data, String categoria) {
        transacoes.add(new Despesa(valor, descricao, data, categoria));
    }

    public void removerTransacao(int indice) {
        if (indice < 0 || indice >= transacoes.size()) {
            throw new IndexOutOfBoundsException("Transação inexistente: " + indice);
        }
        transacoes.remove(indice);
    }

    public double calcularSaldo() {
        double saldo = 0;
        for (Transacao t : transacoes) {
            if (t instanceof Receita) {
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
            if (t.getData().getMonthValue() == mes && t.getData().getYear() == ano) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    /** Visão somente leitura: para alterar, use os métodos desta classe. */
    public List<Transacao> getTransacoes() {
        return Collections.unmodifiableList(transacoes);
    }

    public void setTransacoes(List<Transacao> novas) {
        this.transacoes = (novas == null) ? new ArrayList<>() : new ArrayList<>(novas);
    }
}
