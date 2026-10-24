package controlefinanceiro;

import java.time.LocalDate;

public abstract class Transacao {

    protected double valor;
    protected String descricao;
    protected LocalDate data;
    protected String categoria;

    
    private static int totalTransacoes = 0;

   
    public Transacao(double valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
        this.data = LocalDate.now();
        this.categoria = "Geral";
        totalTransacoes++;
    }

    public Transacao(double valor, String descricao, LocalDate data, String categoria) {
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.categoria = categoria;
        totalTransacoes++;
    }

    
    public abstract String getTipo();

   
    public double getValor() {                                               return valor; }
    public String getDescricao() {                                       return descricao;                                                                               }
    public LocalDate getData() {                                        return data;                                                                                       }
    public String getCategoria() {                                       return categoria;                                                                               }

    public static int getTotalTransacoes() {
        return totalTransacoes;
    }
}