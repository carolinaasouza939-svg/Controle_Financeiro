package controlefinanceiro.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Cadastro e login de usuários em usuarios.txt (uma linha por usuário: nome;senha).
 * As senhas novas são guardadas com hash PBKDF2 (nunca em texto puro).
 * Linhas antigas com senha em texto puro ainda funcionam e são convertidas no primeiro login.
 */
public final class RepositorioUsuarios {

    private static final Path ARQUIVO = Path.of("usuarios.txt");
    private static final String PREFIXO = "$pbkdf2$";
    private static final int ITERACOES = 120_000;
    private static final Pattern NOME_VALIDO = Pattern.compile("[\\p{L}\\p{N}_ .-]{1,30}");

    private RepositorioUsuarios() { }

    /** O nome vira parte do nome de um arquivo, então só aceita caracteres seguros. */
    public static void validarNome(String usuario) {
        if (usuario == null || usuario.isBlank() || usuario.startsWith(".")
                || !NOME_VALIDO.matcher(usuario).matches()) {
            throw new IllegalArgumentException(
                    "Usuário inválido. Use de 1 a 30 caracteres: letras, números, espaço, ponto, hífen ou sublinhado.");
        }
    }

    public static synchronized void cadastrar(String usuario, String senha) throws IOException {
        validarNome(usuario);
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        }
        for (String linha : lerLinhas()) {
            if (nomeDaLinha(linha).equalsIgnoreCase(usuario)) {
                throw new IllegalArgumentException("Usuário já existe!");
            }
        }
        Files.writeString(ARQUIVO, usuario + ";" + gerarHash(senha) + System.lineSeparator(),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static synchronized boolean autenticar(String usuario, String senha) throws IOException {
        if (usuario == null || senha == null) return false;
        List<String> linhas = lerLinhas();
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (!nomeDaLinha(linha).equals(usuario)) continue;

            String guardada = linha.substring(linha.indexOf(';') + 1);
            if (guardada.startsWith(PREFIXO)) {
                return conferirHash(senha, guardada);
            }
            // Formato antigo (texto puro): confere e já converte para hash.
            if (MessageDigest.isEqual(guardada.getBytes(StandardCharsets.UTF_8),
                    senha.getBytes(StandardCharsets.UTF_8))) {
                linhas.set(i, usuario + ";" + gerarHash(senha));
                reescrever(linhas);
                return true;
            }
            return false;
        }
        return false;
    }

    // ---------- internos ----------

    private static List<String> lerLinhas() throws IOException {
        List<String> linhas = new ArrayList<>();
        if (!Files.exists(ARQUIVO)) return linhas;
        for (String l : Files.readAllLines(ARQUIVO, StandardCharsets.UTF_8)) {
            if (!l.isBlank() && l.indexOf(';') > 0) {
                linhas.add(l);
            }
        }
        return linhas;
    }

    private static String nomeDaLinha(String linha) {
        return linha.substring(0, linha.indexOf(';'));
    }

    private static void reescrever(List<String> linhas) throws IOException {
        Path tmp = Path.of("usuarios.txt.tmp");
        Files.write(tmp, linhas, StandardCharsets.UTF_8);
        Files.move(tmp, ARQUIVO, StandardCopyOption.REPLACE_EXISTING);
    }

    private static String gerarHash(String senha) {
        byte[] sal = new byte[16];
        new SecureRandom().nextBytes(sal);
        byte[] hash = pbkdf2(senha, sal, ITERACOES);
        return PREFIXO + ITERACOES + "$"
                + Base64.getEncoder().encodeToString(sal) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    private static boolean conferirHash(String senha, String guardada) {
        try {
            String[] p = guardada.split("\\$"); // "", "pbkdf2", iteracoes, sal, hash
            int iteracoes = Integer.parseInt(p[2]);
            byte[] sal = Base64.getDecoder().decode(p[3]);
            byte[] esperado = Base64.getDecoder().decode(p[4]);
            return MessageDigest.isEqual(esperado, pbkdf2(senha, sal, iteracoes));
        } catch (RuntimeException e) {
            return false; // linha corrompida
        }
    }

    private static byte[] pbkdf2(String senha, byte[] sal, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), sal, iteracoes, 256);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("PBKDF2 indisponível", e);
        }
    }
}
