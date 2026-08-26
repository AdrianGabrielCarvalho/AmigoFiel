package model;

import persistence.GerenciadorArquivos;
import exceptions.AdocaoInvalidaException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAdocoes {
    private List<Adocao> adocoes;

    public RepositorioAdocoes(List<Animal> animais, List<Adotante> adotantes) {
        this.adocoes = GerenciadorArquivos.carregarAdocoes(animais, adotantes);
        if (this.adocoes == null) {
            this.adocoes = new ArrayList<>();
        }
    }

    public void adicionarAdocao(Adocao adocao) throws AdocaoInvalidaException {
        try {
            if (adocao.getAnimalSelecionado().getStatus() != Animal.Status.DISPONIVEL) {
                throw new AdocaoInvalidaException("Animal não está disponível");
            }

            adocoes.add(adocao);
            salvarDados();
        } catch (Exception e) {
            throw new AdocaoInvalidaException("Erro ao adicionar adoção: " + e.getMessage());
        }
    }

    public Adocao buscarPorProtocolo(String protocolo) throws AdocaoInvalidaException {
        Adocao adocao = adocoes.stream()
                .filter(a -> a.getProtocolo().equals(protocolo))
                .findFirst()
                .orElse(null);

        if (adocao == null) {
            throw new AdocaoInvalidaException("Adoção com protocolo " + protocolo + " não encontrada");
        }
        return adocao;
    }

    public boolean removerAdocao(String protocolo) throws AdocaoInvalidaException {
        Adocao adocao = buscarPorProtocolo(protocolo);
        if (adocao != null) {
            adocoes.remove(adocao);
            salvarDados();
            return true;
        }
        return false;
    }

    public List<Adocao> getTodasAdocoes() {
        return new ArrayList<>(adocoes);
    }

    public boolean adotanteTemAdocoes(String cpf) {
        return adocoes.stream()
                .anyMatch(a -> a.getAdotanteResponsavel().getCpf().equals(cpf));
    }

    public boolean animalTemAdocoes(int animalId) {
        return adocoes.stream()
                .anyMatch(a -> a.getAnimalSelecionado().getId() == animalId);
    }

    public void salvarDados() {
        try {
            GerenciadorArquivos.salvarAdocoes(adocoes);
        } catch (Exception e) {
            System.err.println("Erro ao salvar adoções: " + e.getMessage());
        }
    }
}