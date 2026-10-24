package controlefinanceiro.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controlefinanceiro.util.RepositorioUsuarios;
import controlefinanceiro.util.SessaoUsuario;

public class Login extends JFrame {

    private final JTextField campoUsuario;
    private final JPasswordField campoSenha;

    public Login() {
        setTitle("Login - Controle Financeiro");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        // Painel esquerdo
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBackground(new Color(20, 20, 20));

        JLabel tituloSistema = new JLabel("Controle Financeiro Pessoal", SwingConstants.CENTER);
        tituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 26));
        tituloSistema.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Organize sua vida financeira", SwingConstants.CENTER);
        subtitulo.setForeground(Color.LIGHT_GRAY);

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setBackground(new Color(20, 20, 20));
        textos.add(tituloSistema);
        textos.add(subtitulo);
        painelEsquerdo.add(textos, BorderLayout.CENTER);

        // Painel direito (login)
        JPanel painelDireito = new JPanel();
        painelDireito.setBackground(new Color(30, 30, 30));
        painelDireito.setLayout(new BoxLayout(painelDireito, BoxLayout.Y_AXIS));
        painelDireito.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        JLabel icone = new JLabel("\uD83D\uDC64", SwingConstants.CENTER);
        icone.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        icone.setForeground(Color.WHITE);
        icone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloLogin = new JLabel("LOGIN");
        tituloLogin.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloLogin.setForeground(Color.WHITE);
        tituloLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoUsuario = new JTextField();
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoUsuario.setBorder(BorderFactory.createTitledBorder("Usuário"));

        campoSenha = new JPasswordField();
        campoSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoSenha.setBorder(BorderFactory.createTitledBorder("Senha"));

        JButton btnEntrar = new JButton("ENTRAR");
        estilizarBotaoGrande(btnEntrar);

        JButton btnCadastroLink = new JButton("Não tenho cadastro");
        estilizarBotaoLink(btnCadastroLink);

        painelDireito.add(icone);
        painelDireito.add(Box.createRigidArea(new Dimension(0, 15)));
        painelDireito.add(tituloLogin);
        painelDireito.add(Box.createRigidArea(new Dimension(0, 30)));
        painelDireito.add(campoUsuario);
        painelDireito.add(Box.createRigidArea(new Dimension(0, 15)));
        painelDireito.add(campoSenha);
        painelDireito.add(Box.createRigidArea(new Dimension(0, 25)));
        painelDireito.add(btnEntrar);
        painelDireito.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDireito.add(btnCadastroLink);

        add(painelEsquerdo);
        add(painelDireito);

        btnEntrar.addActionListener(e -> fazerLogin());
        getRootPane().setDefaultButton(btnEntrar); // Enter também entra
        btnCadastroLink.addActionListener(e -> {
            new Cadastro().setVisible(true);
            dispose();
        });
    }

    private void estilizarBotaoGrande(JButton btn) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void estilizarBotaoLink(JButton btn) {
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void fazerLogin() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe usuário e senha.");
            return;
        }

        try {
            if (RepositorioUsuarios.autenticar(usuario, senha)) {
                SessaoUsuario.login(usuario);
                new AppFinanceiro().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler os usuários: " + e.getMessage());
        }
    }
}
