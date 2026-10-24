package Controle_Financeiro;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class App_Financeiro extends JFrame {
    private Gerenciador_Financas gerenciador = new Gerenciador_Financas();
    private Arquivo_Transacoes arquivoUtil = new Arquivo_Transacoes();

    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTable tabela;

    private JLabel lblSaldo = new JLabel("Saldo: R$ 0.00");
    private JTextField txtDescrição = new JTextField(10);
    private JTextField txtValor = new JTextField(5);
    private JComboBox<String> cbFiltroCategoria = new JComboBox<>(new String[]{"Todos", "Fixas", "Pontuais", "Extras"});
    private JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Receita", "Despesa"});
    private JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Fixas", "Pontuais", "Extras"});

    public App_Financeiro(){
        setTitle("Controle Financeiro Pessoal");
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        configurarTopo();
        configurarTabela();
        configurarRodape();

        setLocationRelativeTo(null);
    }

    private void configurarTopo() {
        JPanel pnlNorte = new JPanel(new GridLayout(2, 1));

        JPanel pnlAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAdd.setBorder(BorderFactory.createTitledBorder("Nova Transação"));

        pnlAdd.add(new JLabel("Descrição:"));
        pnlAdd.add(txtDescricao);

        pnlAdd.add(new JLabel("Valor:"));
        pnlAdd.add(txtValor);

        pnlAdd.add(new JLabel("Categoria:"));
        pnlAdd.add(cbCategoria);

        pnlAdd.add(new JLabel("Tipo:"));
        pnlAdd.add(cbTipo);

        JButton btnAdd = new JButton("Adicionar");
        btnAdd.addActionListener(e -> acaoAdicionar());
        pnlAdd.add(btnAdd);

        JPanel pnlBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusca.add(new JLabel("Filtrar por Categoria:"));
        pnlBusca.add(cbFiltroCategoria);

        cbFiltroCategoria.addActionListener(e -> {
            String selecionado = (String) cbFiltroCategoria.getSelectedItem();
            if (selecionado.equals("Todos")) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("^" + selecionado + "$", 3));
            }
        });

        pnlNorte.add(pnlAdd);
        pnlNorte.add(pnlBusca);
        add(pnlNorte, BorderLayout.NORTH);
    }

    private void configurarTabela() {
        String[] colunas = {"Tipo", "Descrição", "Valor", "Categoria", "Data"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                int modelRow = t.convertRowIndexToModel(r);
                Object tipoObj = t.getModel().getValueAt(modelRow, 0);

                if (tipoObj != null && !s) {
                    String tipo = tipoObj.toString();
                    comp.setForeground(tipo.equalsIgnoreCase("Receita") ? new Color(0, 128, 0) : Color.RED);
                }
                return comp;
            }
        });

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void configurarRodape() {
        JPanel pnlSul = new JPanel(new BorderLayout(10, 10));
        pnlSul.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        lblSaldo.setFont(new Font("Arial", Font.BOLD, 16));
        pnlSul.add(lblSaldo, BorderLayout.WEST);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRem = new JButton("Remover");
        JButton btnSal = new JButton("Salvar");
        JButton btnCar = new JButton("Carregar");

        btnRem.addActionListener(e -> acaoRemover());
        btnSal.addActionListener(e -> acaoSalvar());
        btnCar.addActionListener(e -> acaoCarregar());

        pnlBotoes.add(btnRem);
        pnlBotoes.add(btnCar);
        pnlBotoes.add(btnSal);
        pnlSul.add(pnlBotoes, BorderLayout.EAST);

        add(pnlSul, BorderLayout.SOUTH);
    }

    private void acaoAdicionar() {
        try {
            if (txtValor.getText().trim().isEmpty() || txtDescrição.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
                return;
            }

            double v = Double.parseDouble(txtValor.getText().replace(",", "."));
            String desc = txtDescrição.getText();
            String cat = (String) cbCategoria.getSelectedItem();
            LocalDate data = LocalDate.now();

            if (cbTipo.getSelectedItem().equals("Receita")) {
                gerenciador.adicionarReceita(v, desc, data, cat);
            } else {
                gerenciador.adicionarDespesa(v, desc, data, cat);
            }

            atualizar();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Digite um número válido no valor.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void acaoRemover() {
        try {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                int modelIndex = tabela.convertRowIndexToModel(row);
                gerenciador.getTransacoes().remove(modelIndex);
                atualizar();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para remover.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover.");
        }
    }

    private void acaoSalvar() {
        try {
            if (gerenciador.getTransacoes().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Não há dados para salvar.");
                return;
            }
            arquivoUtil.salvar(gerenciador.getTransacoes());
            JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo.");
        }
    }

    private void acaoCarregar() {
        try {
            List<Transacao> carregadas = arquivoUtil.carregar();
            if (carregadas != null) {
                gerenciador.setTransacoes(carregadas);
                atualizar();
                JOptionPane.showMessageDialog(this, "Dados carregados com sucesso!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados.");
        }
    }

    private void atualizar() {
        modelo.setRowCount(0);
        for (Transacao t : gerenciador.getTransacoes()) {
            modelo.addRow(new Object[]{
                    t.getTipo(),
                    t.getDescricao(),
                    String.format("%.2f", t.getValor()),
                    t.getCategoria(),
                    t.getData(),
            });
        }

        double saldoTotal = gerenciador.calcularSaldo();
        lblSaldo.setText(String.format("Saldo Atual: R$ %.2f", saldoTotal));
        lblSaldo.setForeground(saldoTotal >= 0 ? new Color(0, 128, 0) : Color.RED);
    }

    private void limparCampos() {
        txtDescricao.setText("");
        txtValor.setText("");
        cbCategoria.setSelectedIndex(0);
        txtDescricao.requestFocus();
    }
}
