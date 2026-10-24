package controlefinanceiro.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controlefinanceiro.Despesa;
import controlefinanceiro.Receita;
import controlefinanceiro.Transacao;

public class ArquivoTransacoes {

    private String caminho;

    public ArquivoTransacoes(String caminho) {
        this.caminho = caminho;
    }

    public void salvar(List<Transacao> transacoes) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(caminho));

        for (Transacao t : transacoes) {
            writer.println(
                t.getTipo() + ";" +
                t.getValor() + ";" +
                t.getDescricao() + ";" +
                t.getData() + ";" +
                t.getCategoria()
            );
        }

        writer.close();
    }

    public List<Transacao> carregar() throws Exception {
        List<Transacao> lista = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(caminho));
        String linha;

        while ((linha = reader.readLine()) != null) {
            String[] partes = linha.split(";");

            String tipo = partes[0];
            double valor = Double.parseDouble(partes[1]);
            String descricao = partes[2];
            LocalDate data = LocalDate.parse(partes[3]);
            String categoria = partes[4];

            if (tipo.equals("Receita")) {
                lista.add(new Receita(valor, descricao, data, categoria));
            } else {
                lista.add(new Despesa(valor, descricao, data, categoria));
            }
        }

        reader.close();
        return lista;
    }
}
