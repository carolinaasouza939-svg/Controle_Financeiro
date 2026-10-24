package controlefinanceiro.model;

import java.time.LocalDate;

public abstract class Transacao {

    private static int totalTransacoes = 0;

    protected final double valor;
    protected final String descricao;
    protected final LocalDate data;
    protected final String categoria;

    public Transacao(double valor, String descricao) {
        this(valor, descricao, LocalDate.now(), "Sem Categoria");
    }

    public Transacao(double valor, String descricao, LocalDate data, String categoria) {
        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição não pode ser vazia.");
        }
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula.");
        }
        this.valor = valor;
        this.descricao = descricao.trim();
        this.data = data;
        this.categoria = (categoria == null || categoria.isBlank()) ? "Sem Categoria" : categoria.trim();
        totalTransacoes++;
    }

    public abstract String getTipo();

    public double getValor() { return valor; }
    public String getDescricao() { return descricao; }
    public LocalDate getData() { return data; }
    public String getCategoria() { return categoria; }

    public static int getTotalTransacoes() { return totalTransacoes; }
}
