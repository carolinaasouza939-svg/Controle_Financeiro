package controlefinanceiro.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controlefinanceiro.controller.GerenciadorFinancas;
import controlefinanceiro.model.ResumoFinanceiro;
import controlefinanceiro.model.Transacao;
import controlefinanceiro.util.ArquivoTransacoes;
import controlefinanceiro.util.SessaoUsuario;

public class AppFinanceiro extends JFrame {

    private static final int COL_VALOR = 2;
    private static final int COL_DATA = 4;

    private final GerenciadorFinancas gerenciador = new GerenciadorFinancas();
    private final ArquivoTransacoes arquivoUtil;
    private boolean alteracoesNaoSalvas = false;

    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTable tabela;

    private final JLabel lblSaldo = new JLabel("Saldo Atual: R$ 0,00");
    private final JTextField txtDescricao = new JTextField(12);
    private final JTextField txtValor = new JTextField(6);
    private final JTextField txtBusca = new JTextField(15);
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Receita", "Despesa"});
    private final JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"Fixas", "Pontuais", "Extras"});

    public AppFinanceiro() {
        setTitle("Controle Financeiro Pessoal - " + SessaoUsuario.getUsuarioLogado());
        setSize(900, 550);
        setLayout(new BorderLayout(10, 10));

        // Perguntar antes de fechar se houver alterações não salvas.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmarSaida()) {
                    System.exit(0);
                }
            }
        });

        arquivoUtil = new ArquivoTransacoes(SessaoUsuario.getUsuarioLogado());

        configurarTopo();
        configurarTabela();
        configurarRodape();

        // Carrega as transações do usuário ao abrir e mostra na tabela.
        try {
            gerenciador.setTransacoes(arquivoUtil.carregar());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar transações do usuário: " + e.getMessage());
        }
        atualizar();

        setLocationRelativeTo(null);
    }

    // ---------- montagem da tela ----------

    private JComponent criarBotaoVoltar() {
        JLabel btnVoltar;
        URL url = getClass().getResource("/img/bntvolta.png");
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            btnVoltar = new JLabel(new ImageIcon(img));
        } else {
            btnVoltar = new JLabel("\u2190 Voltar"); // imagem não encontrada: usa texto
        }
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.setToolTipText("Voltar para o login");
        btnVoltar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (confirmarSaida()) {
                    SessaoUsuario.logout();
                    new Login().setVisible(true);
                    dispose();
                }
            }
        });
        return btnVoltar;
    }

    private void configurarTopo() {
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        JPanel painelSeta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelSeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        painelSeta.add(criarBotaoVoltar());
        painelTopo.add(painelSeta);
        painelTopo.add(Box.createVerticalStrut(10));

        JPanel painelAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAdd.setBorder(BorderFactory.createTitledBorder("Nova Transação"));
        painelAdd.add(new JLabel("Descrição:"));
        painelAdd.add(txtDescricao);
        painelAdd.add(new JLabel("Valor:"));
        painelAdd.add(txtValor);
        painelAdd.add(new JLabel("Categoria:"));
        painelAdd.add(cbCategoria);
        painelAdd.add(new JLabel("Tipo:"));
        painelAdd.add(cbTipo);

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> acaoAdicionar());
        painelAdd.add(btnAdicionar);
        painelTopo.add(painelAdd);

        JPanel pnlBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusca.add(new JLabel("Buscar:"));
        pnlBusca.add(txtBusca);
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarBusca(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarBusca(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarBusca(); }
        });
        painelTopo.add(pnlBusca);

        add(painelTopo, BorderLayout.NORTH);
    }

    private void aplicarBusca() {
        String filtro = txtBusca.getText().trim();
        // Pattern.quote: o que o usuário digita é texto, não expressão regular.
        sorter.setRowFilter(filtro.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(filtro)));
    }

    private void configurarTabela() {
        String[] colunas = {"Tipo", "Descrição", "Valor", "Categoria", "Data"};

        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }

            // Com o tipo certo da coluna, "Valor" e "Data" ordenam como número/data (e não como texto).
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case COL_VALOR -> Double.class;
                    case COL_DATA -> LocalDate.class;
                    default -> String.class;
                };
            }
        };

        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean foc, int r, int c) {
                Object valor = (v instanceof Double d) ? String.format("%.2f", d) : v;
                Component comp = super.getTableCellRendererComponent(t, valor, sel, foc, r, c);
                setHorizontalAlignment(v instanceof Double ? SwingConstants.RIGHT : SwingConstants.LEFT);
                if (!sel) {
                    Object tipo = t.getModel().getValueAt(t.convertRowIndexToModel(r), 0);
                    comp.setForeground("Receita".equals(tipo) ? new Color(0, 128, 0) : Color.RED);
                }
                return comp;
            }
        };
        tabela.setDefaultRenderer(Object.class, renderer);
        tabela.setDefaultRenderer(Double.class, renderer); // o Swing tem um renderer próprio para Double

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
        JButton btnResumo = new JButton("Resumo do Mês");

        btnRem.addActionListener(e -> acaoRemover());
        btnSal.addActionListener(e -> salvar());
        btnCar.addActionListener(e -> acaoCarregar());
        btnResumo.addActionListener(e -> acaoResumoMes());

        pnlBotoes.add(btnRem);
        pnlBotoes.add(btnCar);
        pnlBotoes.add(btnSal);
        pnlBotoes.add(btnResumo);
        pnlSul.add(pnlBotoes, BorderLayout.EAST);

        add(pnlSul, BorderLayout.SOUTH);
    }

    // ---------- ações ----------

    private void acaoAdicionar() {
        String desc = txtDescricao.getText().trim();
        String textoValor = txtValor.getText().trim();
        if (desc.isEmpty() || textoValor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha descrição e valor.");
            return;
        }
        try {
            double valor = Double.parseDouble(textoValor.replace(",", "."));
            String cat = (String) cbCategoria.getSelectedItem();
            LocalDate data = LocalDate.now();

            if ("Receita".equals(cbTipo.getSelectedItem())) {
                gerenciador.adicionarReceita(valor, desc, data, cat);
            } else {
                gerenciador.adicionarDespesa(valor, desc, data, cat);
            }
            alteracoesNaoSalvas = true;
            atualizar();
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: digite um número válido no valor.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void acaoRemover() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para remover.");
            return;
        }
        gerenciador.removerTransacao(tabela.convertRowIndexToModel(row));
        alteracoesNaoSalvas = true;
        atualizar();
    }

    /** @return true se salvou com sucesso. */
    private boolean salvar() {
        try {
            arquivoUtil.salvar(gerenciador.getTransacoes());
            alteracoesNaoSalvas = false;
            JOptionPane.showMessageDialog(this, "Dados salvos!");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
            return false;
        }
    }

    private void acaoCarregar() {
        if (alteracoesNaoSalvas) {
            int r = JOptionPane.showConfirmDialog(this,
                    "Carregar vai descartar as alterações não salvas. Continuar?",
                    "Carregar", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) return;
        }
        try {
            gerenciador.setTransacoes(arquivoUtil.carregar());
            alteracoesNaoSalvas = false;
            atualizar();
            JOptionPane.showMessageDialog(this, "Dados carregados!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage());
        }
    }

    private void acaoResumoMes() {
        LocalDate hoje = LocalDate.now();
        JSpinner spMes = new JSpinner(new SpinnerNumberModel(hoje.getMonthValue(), 1, 12, 1));
        JSpinner spAno = new JSpinner(new SpinnerNumberModel(hoje.getYear(), 1900, 2200, 1));
        spAno.setEditor(new JSpinner.NumberEditor(spAno, "#")); // sem separador de milhar

        JPanel campos = new JPanel(new GridLayout(2, 2, 5, 5));
        campos.add(new JLabel("Mês (1 a 12):"));
        campos.add(spMes);
        campos.add(new JLabel("Ano:"));
        campos.add(spAno);

        int ok = JOptionPane.showConfirmDialog(this, campos, "Resumo do mês",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;

        int mes = (Integer) spMes.getValue();
        int ano = (Integer) spAno.getValue();
        List<Transacao> doMes = gerenciador.filtrarPorMes(mes, ano);

        if (doMes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma transação nesse mês.");
            return;
        }

        ResumoFinanceiro resumo = new ResumoFinanceiro(doMes);
        String periodo = String.format("%02d/%d", mes, ano);
        String texto = String.format("Resumo de %s:%n%nReceitas: R$ %.2f%nDespesas: R$ %.2f%nSaldo: R$ %.2f",
                periodo, resumo.totalEntradas(), resumo.totalSaidas(), resumo.saldo());

        Object[] opcoes = {"Ver gráfico", "Fechar"};
        int escolha = JOptionPane.showOptionDialog(this, texto, "Resumo do mês",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == 0) {
            new TelaGrafico(resumo, periodo).setVisible(true);
        }
    }

    /** Pergunta se quer salvar quando há alterações pendentes. @return false se o usuário cancelou. */
    private boolean confirmarSaida() {
        if (!alteracoesNaoSalvas) return true;
        int r = JOptionPane.showConfirmDialog(this,
                "Há alterações não salvas. Deseja salvar antes de sair?",
                "Alterações não salvas", JOptionPane.YES_NO_CANCEL_OPTION);
        if (r == JOptionPane.YES_OPTION) return salvarSilencioso();
        return r == JOptionPane.NO_OPTION;
    }

    private boolean salvarSilencioso() {
        try {
            arquivoUtil.salvar(gerenciador.getTransacoes());
            alteracoesNaoSalvas = false;
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
            return false;
        }
    }

    // ---------- atualização da tela ----------

    private void atualizar() {
        modelo.setRowCount(0);
        for (Transacao t : gerenciador.getTransacoes()) {
            modelo.addRow(new Object[]{
                    t.getTipo(),
                    t.getDescricao(),
                    t.getValor(),
                    t.getCategoria(),
                    t.getData()
            });
        }
        double saldo = gerenciador.calcularSaldo();
        lblSaldo.setText(String.format("Saldo Atual: R$ %.2f", saldo));
        lblSaldo.setForeground(saldo >= 0 ? new Color(0, 128, 0) : Color.RED);
    }

    private void limparCampos() {
        txtDescricao.setText("");
        txtValor.setText("");
        cbCategoria.setSelectedIndex(0);
        txtDescricao.requestFocus();
    }
}
