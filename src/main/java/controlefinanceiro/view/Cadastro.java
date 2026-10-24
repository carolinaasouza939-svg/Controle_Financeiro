package controlefinanceiro.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controlefinanceiro.util.RepositorioUsuarios;

public class Cadastro extends JFrame {

    private final JTextField campoUsuario;
    private final JPasswordField campoSenha;

    public Cadastro() {
        setTitle("Cadastro de Usuário");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(30, 30, 30));
        JLabel titulo = new JLabel("Cadastro de Usuário");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        painelTopo.add(titulo);
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(new Color(40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCentral.add(lblUsuario, gbc);

        campoUsuario = new JTextField(20);
        gbc.gridx = 1;
        painelCentral.add(campoUsuario, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelCentral.add(lblSenha, gbc);

        campoSenha = new JPasswordField(20);
        gbc.gridx = 1;
        painelCentral.add(campoSenha, gbc);

        add(painelCentral, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel();
        painelInferior.setBackground(new Color(30, 30, 30));

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(Color.BLACK);
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.addActionListener(e -> cadastrarUsuario());

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(Color.DARK_GRAY);
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        painelInferior.add(btnVoltar);
        painelInferior.add(Box.createRigidArea(new Dimension(20, 0)));
        painelInferior.add(btnCadastrar);
        add(painelInferior, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnCadastrar);
    }

    private void cadastrarUsuario() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        try {
            RepositorioUsuarios.cadastrar(usuario, senha);
            JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso!\nFaça login para acessar o sistema.");
            new Login().setVisible(true);
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
}
