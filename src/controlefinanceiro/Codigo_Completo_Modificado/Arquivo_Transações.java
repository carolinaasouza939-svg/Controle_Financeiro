package Controle_Financeiro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Arquivo_Transacoes {

    private String caminho = "transacoes.csv";
    //o arquivo fica salvo com esse nome;

    public Arquivo_Transacoes() {
    }

    //PrintWriter e FileWriter são usados no metodo "Salvar", servem para escrever os dados das transações no arquivo;
    public void salvar(List<Transacao> transacoes) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            for (Transacao t : transacoes) {
                writer.println(
                        t.getTipo() + ";" +
                                t.getDescricao() + ";" +
                                t.getValor() + ";" +
                                t.getCategoria() + ";" +
                                t.getData()
                );
            }
        }
    }

    //FileReader e BufferedReader são usados no metodo "Carregar", servem para ler o arquivo linha por linha;
    public List<Transacao> carregar() throws Exception {
        List<Transacao> lista = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {

                //ignora linhas em branco no arquivo;
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(";");

                //verifica se a linha tem todas as colunas necessárias;
                if (partes.length < 5) {
                    System.err.println("Linha incompleta ignorada: " + linha);
                    continue;
                }

                //try-catch interno para capturar erros de conversão (valor ou data);
                try {
                    String tipo = partes[0];      
                    String descricao = partes[1]; 
                    double valor = Double.parseDouble(partes[2]); 
                    String categoria = partes[3]; 
                    LocalDate data = LocalDate.parse(partes[4]);  

                    if (tipo.equalsIgnoreCase("Receita")) {
                        lista.add(new Receita(valor, descricao, data, categoria));
                    } else {
                        lista.add(new Despesa(valor, descricao, data, categoria));
                    }
                } catch (Exception e) {
                    //se uma linha estiver corrompida, avisa no console e pula para a próxima;
                    System.err.println("Erro ao converter dados da linha: " + linha);
                }
            }
        } catch (java.io.FileNotFoundException e) {
            //se o arquivo ainda não existir, apenas retorna a lista vazia sem dar erro;
            System.out.println("Arquivo não encontrado. Iniciando nova lista.");
        }

        return lista;
    }
}
