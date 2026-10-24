package controlefinanceiro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import controlefinanceiro.model.Despesa;
import controlefinanceiro.model.Receita;
import controlefinanceiro.model.Transacao;

/**
 * Guarda as transações de um usuário em usuarios/transacoes_NOME.txt.
 * Formato de cada linha (o mesmo dos arquivos que você já tem):
 * Tipo;Valor;Descricao;Data;Categoria
 */
public class ArquivoTransacoes {

    private final Path caminho;

    public ArquivoTransacoes(String usuario) {
        RepositorioUsuarios.validarNome(usuario);
        this.caminho = Path.of("usuarios", "transacoes_" + usuario + ".txt");
    }

    public void salvar(List<Transacao> transacoes) throws IOException {
        Files.createDirectories(caminho.getParent());
        try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(caminho, StandardCharsets.UTF_8))) {
            for (Transacao t : transacoes) {
                w.println(String.join(";",
                        t.getTipo(),
                        String.format(Locale.US, "%.2f", t.getValor()),
                        limpar(t.getDescricao()),
                        t.getData().toString(),
                        limpar(t.getCategoria())));
            }
        }
    }

    public List<Transacao> carregar() throws IOException {
        List<Transacao> lista = new ArrayList<>();
        if (!Files.exists(caminho)) {
            return lista;
        }
        try (BufferedReader r = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = r.readLine()) != null) {
                if (linha.isBlank()) continue;
                String[] p = linha.split(";", -1);
                if (p.length < 5) {
                    System.err.println("Linha incompleta ignorada: " + linha);
                    continue;
                }
                try {
                    double valor = Double.parseDouble(p[1].trim().replace(",", "."));
                    LocalDate data = LocalDate.parse(p[3].trim());
                    lista.add(p[0].trim().equalsIgnoreCase("Receita")
                            ? new Receita(valor, p[2], data, p[4])
                            : new Despesa(valor, p[2], data, p[4]));
                } catch (RuntimeException e) {
                    System.err.println("Linha inválida ignorada: " + linha);
                }
            }
        }
        return lista;
    }

    /** O ';' é o separador, então não pode aparecer dentro dos textos. */
    private static String limpar(String s) {
        return s.replace(';', ',').replace('\n', ' ').replace('\r', ' ');
    }
}
