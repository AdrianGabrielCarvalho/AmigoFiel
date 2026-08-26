package control;

import model.Adocao;
import model.Animal;
import model.Adotante;
import model.RepositorioAdocoes;
import exceptions.*;
import java.util.ArrayList;
import java.util.List;

public class AdocaoController {
    private RepositorioAdocoes repositorio;
    private AnimalController animalController;
    private AdotanteController adotanteController;

    public AdocaoController(AnimalController animalController,
                            AdotanteController adotanteController,
                            RepositorioAdocoes repositorio) {
        this.repositorio = repositorio;
        this.animalController = animalController;
        this.adotanteController = adotanteController;
    }

    public Adocao registrarAdocao(int animalId, String cpfAdotante)
            throws AnimalNaoEncontradoException,
            AdotanteNaoEncontradoException,
            AdocaoInvalidaException {
        try {
            Animal animal = animalController.buscarPorId(animalId);
            Adotante adotante = adotanteController.buscarPorCpf(cpfAdotante);

            if (animal.getStatus() != Animal.Status.DISPONIVEL) {
                throw new AdocaoInvalidaException("Animal não está disponível");
            }

            if (adotanteTemAdocaoAtiva(cpfAdotante)) {
                throw new AdocaoInvalidaException("Este adotante já possui uma adoção");
            }

            Adocao adocao = new Adocao(animal, adotante);
            repositorio.adicionarAdocao(adocao);

            animalController.atualizarStatus(animalId, Animal.Status.EM_PROCESSO);

            return adocao;
        } catch (AnimalNaoEncontradoException | AdotanteNaoEncontradoException |
                 AdocaoInvalidaException e) {
            throw e;
        } catch (Exception e) {
            throw new AdocaoInvalidaException("Erro interno no registro de adoção");
        }
    }

    public boolean concluirAdocao(String protocolo) throws AdocaoInvalidaException {
        try {
            Adocao adocao = repositorio.buscarPorProtocolo(protocolo);
            if (adocao != null && !adocao.isConcluida()) {
                adocao.setConcluida(true);

                try {
                    String cpfAdotante = adocao.getAdotanteResponsavel().getCpf();
                    adotanteController.removerAdotante(cpfAdotante);
                } catch (AdotanteNaoEncontradoException e) {
                    System.out.println("Adotante já removido: " + e.getMessage());
                }

                repositorio.salvarDados();
                return true;
            }
            return false;
        } catch (AdocaoInvalidaException e) {
            throw e;
        } catch (Exception e) {
            throw new AdocaoInvalidaException("Erro interno na conclusão de adoção");
        }
    }

    public boolean removerAdocao(String protocolo) throws AdocaoInvalidaException {
        try {
            Adocao adocao = repositorio.buscarPorProtocolo(protocolo);
            if (adocao != null) {
                if (!adocao.isConcluida()) {
                    adocao.getAnimalSelecionado().setStatus(Animal.Status.DISPONIVEL);
                    return repositorio.removerAdocao(protocolo);
                } else {
                    throw new AdocaoInvalidaException("Não é possível cancelar uma adoção já concluída");
                }
            }
            return false;
        } catch (AdocaoInvalidaException e) {
            throw e;
        } catch (Exception e) {
            throw new AdocaoInvalidaException("Erro interno na remoção de adoção");
        }
    }

    public List<Animal> getAnimaisCompativeis(String cpfAdotante) throws AdotanteNaoEncontradoException {
        try {
            Adotante adotante = adotanteController.buscarPorCpf(cpfAdotante);
            List<Animal> animaisCompativeis = new ArrayList<>();
            List<Animal> animaisDisponiveis = animalController.listarDisponiveis();

            for (Animal animal : animaisDisponiveis) {
                if (SistemaCompatibilidade.verificarCompatibilidade(animal, adotante)) {
                    animaisCompativeis.add(animal);
                }
            }

            return animaisCompativeis;
        } catch (AdotanteNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao buscar animais compatíveis: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Adocao> getTodasAdocoes() {
        try {
            return repositorio.getTodasAdocoes();
        } catch (Exception e) {
            System.err.println("Erro ao buscar adoções: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Adocao buscarAdocaoPorProtocolo(String protocolo) throws AdocaoInvalidaException {
        return repositorio.buscarPorProtocolo(protocolo);
    }

    public boolean adotanteTemAdocoes(String cpf) {
        try {
            return repositorio.adotanteTemAdocoes(cpf);
        } catch (Exception e) {
            System.err.println("Erro ao verificar adoções do adotante: " + e.getMessage());
            return false;
        }
    }

    public boolean animalTemAdocoes(int animalId) {
        try {
            return repositorio.animalTemAdocoes(animalId);
        } catch (Exception e) {
            System.err.println("Erro ao verificar adoções do animal: " + e.getMessage());
            return false;
        }
    }

    private boolean adotanteTemAdocaoAtiva(String cpf) {
        try {
            List<Adocao> todasAdocoes = repositorio.getTodasAdocoes();
            for (Adocao adocao : todasAdocoes) {
                if (adocao.getAdotanteResponsavel().getCpf().equals(cpf)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao verificar adoções ativas do adotante: " + e.getMessage());
            return false;
        }
    }
}