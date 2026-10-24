package controlefinanceiro;

import java.time.LocalDate;

public class Despesa extends Transacao {

    public Despesa(double valor, String descricao) {
        super(valor, descricao);
    }

    public Despesa(double valor, String descricao, LocalDate data, String categoria) {
        super(valor, descricao, data, categoria);
    }

    @Override
    public String getTipo() {
        return "Despesa";
    }
}