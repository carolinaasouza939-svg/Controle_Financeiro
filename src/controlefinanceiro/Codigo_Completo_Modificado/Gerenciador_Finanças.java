package Controle_Financeiro;
//tirei a estrutura de subpastas, agora é só uma;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Tirei os imports de Receita, Despesa e Transação, já que todas as classes estão no mesmo package;
public class Gerenciador_Financas {

    private List<Transacao> transacoes;

    public Gerenciador_Financas(){
        transacoes = new ArrayList<>();
    }

    public void adicionarReceita(double valor, String descricao){
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
