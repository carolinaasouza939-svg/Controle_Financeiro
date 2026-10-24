package controlefinanceiro.util;

public final class SessaoUsuario {

    private static String usuarioLogado;

    private SessaoUsuario() { }

    public static void login(String usuario) {
        usuarioLogado = usuario;
    }

    public static void logout() {
        usuarioLogado = null;
    }

    public static String getUsuarioLogado() {
        return usuarioLogado;
    }
}
