package Controle_Financeiro;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args){
        //configura a aparencia da Interface;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //serve pra mandar o Java utilizar o visual nativo do SO (seja do Windows ou do macOS);
        } catch (Exception e) { e.printStackTrace();
        }
        //é pra interface ser criada no EDT;
        //Event Dispatch Thread = Thread de Despacho de Eventos, é responsavel pelos eventos (Cliques, teclas e etc);
        SwingUtilities.invokeLater(() -> {
            App_Financeiro gui = new App_Financeiro(); //cria o objeto;
            gui.setVisible(true); //faz aparecer na tela do usuario;
        });
    }
}
