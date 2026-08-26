package persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Arquivo {

    public static void escreverArquivo(String nomeArquivo, String conteudo) {
        Path caminho = Path.of(nomeArquivo);

        try {
            Files.writeString(caminho, conteudo);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }

    public static List<String> lerArquivo(String nomeArquivo) {
        List<String> linhasDoArquivo = new LinkedList<>();

        try {
            if (Files.exists(Paths.get(nomeArquivo))) {
                List<String> linhasLidas = Files.readAllLines(Paths.get(nomeArquivo));
                linhasDoArquivo.addAll(linhasLidas);
            }
        } catch (IOException e) {
            System.err.println(" Erro ao ler arquivo: " + e.getMessage());
        }

        return linhasDoArquivo;
    }

    public static boolean arquivoExiste(String nomeArquivo) {
        return Files.exists(Paths.get(nomeArquivo));
    }

    public static void criarDiretorioSeNaoExistir(String nomeDiretorio) {
        Path diretorio = Paths.get(nomeDiretorio);
        if (!Files.exists(diretorio)) {
            try {
                Files.createDirectory(diretorio);
            } catch (IOException e) {
                System.err.println("Erro ao criar diretório: " + e.getMessage());
            }
        }
    }
}