package persistence;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {
    private static final String DIRETORIO_DADOS = "txt";
    private static final String ARQUIVO_ANIMAIS = "txt/animais.txt";
    private static final String ARQUIVO_ADOTANTES = "txt/adotantes.txt";
    private static final String ARQUIVO_ADOCOES = "txt/adocoes.txt";

    static {
        Arquivo.criarDiretorioSeNaoExistir(DIRETORIO_DADOS);
    }

    public static void salvarAnimais(List<Animal> animais) {
        StringBuilder conteudo = new StringBuilder();
        for (Animal animal : animais) {
            conteudo.append(animal.getId()).append(";")
                    .append(animal.getNome()).append(";")
                    .append(animal.getEspecie()).append(";")
                    .append(animal.getIdade()).append(";")
                    .append(animal.getPorte()).append(";")
                    .append(animal.getTemperamento()).append(";")
                    .append(animal.getStatus()).append("\n");
        }
        Arquivo.escreverArquivo(ARQUIVO_ANIMAIS, conteudo.toString());
    }

    public static List<Animal> carregarAnimais() {
        List<Animal> animais = new ArrayList<>();
        List<String> linhas = Arquivo.lerArquivo(ARQUIVO_ANIMAIS);

        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                try {
                    String[] dados = linha.split(";");
                    if (dados.length >= 7) {
                        int id = Integer.parseInt(dados[0]);
                        String nome = dados[1];
                        Animal.Especie especie = Animal.Especie.valueOf(dados[2]);
                        int idade = Integer.parseInt(dados[3]);
                        Animal.Porte porte = Animal.Porte.valueOf(dados[4]);
                        Animal.Temperamento temperamento = Animal.Temperamento.valueOf(dados[5]);
                        Animal.Status status = Animal.Status.valueOf(dados[6]);

                        Animal animal = new Animal(id, nome, especie, idade, porte, temperamento);
                        animal.setStatus(status);
                        animais.add(animal);
                    }
                } catch (Exception e) {
                    System.err.println("⚠️  Erro ao processar linha: " + linha);
                }
            }
        }

        return animais;
    }

    public static void salvarAdotantes(List<Adotante> adotantes) {
        StringBuilder conteudo = new StringBuilder();
        for (Adotante adotante : adotantes) {
            String preferencia = (adotante.getPreferenciaPorte() == null) ?
                    "null" : adotante.getPreferenciaPorte().toString();
            conteudo.append(adotante.getCpf()).append(";")
                    .append(adotante.getNome()).append(";")
                    .append(adotante.getTelefone()).append(";")
                    .append(adotante.getTipoMoradia()).append(";")
                    .append(preferencia).append("\n");
        }
        Arquivo.escreverArquivo(ARQUIVO_ADOTANTES, conteudo.toString());
    }

    public static List<Adotante> carregarAdotantes() {
        List<Adotante> adotantes = new ArrayList<>();
        List<String> linhas = Arquivo.lerArquivo(ARQUIVO_ADOTANTES);

        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                try {
                    String[] dados = linha.split(";");
                    if (dados.length >= 5) {
                        String cpf = dados[0];
                        String nome = dados[1];
                        String telefone = dados[2];
                        Adotante.TipoMoradia tipoMoradia = Adotante.TipoMoradia.valueOf(dados[3]);
                        Animal.Porte preferenciaPorte = dados[4].equals("null") ? null :
                                Animal.Porte.valueOf(dados[4]);

                        Adotante adotante = new Adotante(cpf, nome, telefone, tipoMoradia, preferenciaPorte);
                        adotantes.add(adotante);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha: " + linha);
                }
            }
        }

        return adotantes;
    }

    public static void salvarAdocoes(List<Adocao> adocoes) {
        StringBuilder conteudo = new StringBuilder();
        for (Adocao adocao : adocoes) {
            conteudo.append(adocao.getProtocolo()).append(";")
                    .append(adocao.getAnimalSelecionado().getId()).append(";")
                    .append(adocao.getAdotanteResponsavel().getCpf()).append(";")
                    .append(adocao.getData()).append(";")
                    .append(adocao.isConcluida()).append("\n");
        }
        Arquivo.escreverArquivo(ARQUIVO_ADOCOES, conteudo.toString());
    }

    public static List<Adocao> carregarAdocoes(List<Animal> animais, List<Adotante> adotantes) {
        List<Adocao> adocoes = new ArrayList<>();
        List<String> linhas = Arquivo.lerArquivo(ARQUIVO_ADOCOES);

        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                try {
                    String[] dados = linha.split(";");
                    if (dados.length >= 5) {
                        String protocolo = dados[0];
                        int animalId = Integer.parseInt(dados[1]);
                        String cpfAdotante = dados[2];
                        String dataStr = dados[3];
                        boolean concluida = Boolean.parseBoolean(dados[4]);

                        Animal animal = encontrarAnimalPorId(animais, animalId);
                        Adotante adotante = encontrarAdotantePorCpf(adotantes, cpfAdotante);

                        if (animal != null && adotante != null) {
                            Adocao adocao = new Adocao(animal, adotante);
                            adocao.setProtocolo(protocolo);
                            adocao.setConcluida(concluida);
                            adocoes.add(adocao);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar adoção: " + linha);
                }
            }
        }

        return adocoes;
    }

    private static Animal encontrarAnimalPorId(List<Animal> animais, int id) {
        return animais.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private static Adotante encontrarAdotantePorCpf(List<Adotante> adotantes, String cpf) {
        return adotantes.stream()
                .filter(a -> a.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }
}