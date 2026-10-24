package controlefinanceiro;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controlefinanceiro.view.Login;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // A interface deve ser criada na thread de eventos do Swing (EDT).
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
