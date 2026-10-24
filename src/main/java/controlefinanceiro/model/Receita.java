package controlefinanceiro.model;

import java.time.LocalDate;

public class Receita extends Transacao {

    public Receita(double valor, String descricao) {
        super(valor, descricao);
    }

    public Receita(double valor, String descricao, LocalDate data, String categoria) {
        super(valor, descricao, data, categoria);
    }

    @Override
    public String getTipo() {
        return "Receita";
    }
}
