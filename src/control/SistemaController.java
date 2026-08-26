package control;

import model.*;
import exceptions.*;
import java.util.List;

public class SistemaController {
    private RepositorioAnimais repositorioAnimais;
    private RepositorioAdotantes repositorioAdotantes;
    private RepositorioAdocoes repositorioAdocoes;
    private AnimalController animalController;
    private AdotanteController adotanteController;
    private AdocaoController adocaoController;

    public SistemaController() {
        try {
            this.repositorioAnimais = new RepositorioAnimais();
            this.repositorioAdotantes = new RepositorioAdotantes();
            this.repositorioAdocoes = new RepositorioAdocoes(
                    repositorioAnimais.getTodosAnimais(),
                    repositorioAdotantes.getTodosAdotantes()
            );

            this.animalController = new AnimalController(repositorioAnimais);
            this.adotanteController = new AdotanteController(repositorioAdotantes);
            this.adocaoController = new AdocaoController(
                    animalController,
                    adotanteController,
                    repositorioAdocoes
            );
        } catch (Exception e) {
            throw new RuntimeException("Falha na inicialização do sistema", e);
        }
    }

    // Animal
    public Animal cadastrarAnimal(String nome, Animal.Especie especie, int idade,
                                  Animal.Porte porte, Animal.Temperamento temperamento)
            throws IllegalArgumentException {
        return animalController.cadastrarAnimal(nome, especie, idade, porte, temperamento);
    }

    public List<Animal> listarAnimaisDisponiveis() {
        return animalController.listarDisponiveis();
    }

    public List<Animal> getTodosAnimais() {
        return animalController.getTodosAnimais();
    }

    public List<Animal> filtrarAnimaisPorEspecie(Animal.Especie especie) {
        return animalController.filtrarPorEspecie(especie);
    }

    public Animal buscarAnimalPorId(int id) throws AnimalNaoEncontradoException {
        return animalController.buscarPorId(id);
    }

    public boolean removerAnimal(int id) throws AnimalNaoEncontradoException, IllegalStateException {
        if (adocaoController.animalTemAdocoes(id)) {
            throw new IllegalStateException("❌ Animal está envolvido em uma adoção");
        }
        return animalController.removerAnimal(id);
    }

    // Adotante
    public void cadastrarAdotante(String cpf, String nome, String telefone,
                                  model.Adotante.TipoMoradia tipoMoradia,
                                  Animal.Porte preferenciaPorte)
            throws IllegalArgumentException {
        adotanteController.cadastrarAdotante(cpf, nome, telefone, tipoMoradia, preferenciaPorte);
    }

    public List<Adotante> getTodosAdotantes() {
        return adotanteController.getTodosAdotantes();
    }

    public Adotante buscarAdotantePorCpf(String cpf) throws AdotanteNaoEncontradoException {
        return adotanteController.buscarPorCpf(cpf);
    }

    public boolean removerAdotante(String cpf) throws AdotanteNaoEncontradoException, IllegalStateException {
        if (adocaoController.adotanteTemAdocoes(cpf)) {
            throw new IllegalStateException("❌ Adotante está envolvido em uma adoção");
        }
        return adotanteController.removerAdotante(cpf);
    }

    // Adoção
    public Adocao registrarAdocao(int animalId, String cpfAdotante)
            throws AnimalNaoEncontradoException,
            AdotanteNaoEncontradoException,
            AdocaoInvalidaException {
        return adocaoController.registrarAdocao(animalId, cpfAdotante);
    }

    public boolean concluirAdocao(String protocolo) throws AdocaoInvalidaException {
        return adocaoController.concluirAdocao(protocolo);
    }

    public boolean removerAdocao(String protocolo) throws AdocaoInvalidaException {
        return adocaoController.removerAdocao(protocolo);
    }

    public List<Adocao> getTodasAdocoes() {
        return adocaoController.getTodasAdocoes();
    }

    public List<Animal> getAnimaisCompativeis(String cpfAdotante) throws AdotanteNaoEncontradoException {
        return adocaoController.getAnimaisCompativeis(cpfAdotante);
    }

    // Compatibilidade
    public String verificarCompatibilidade(Animal animal, Adotante adotante) {
        return SistemaCompatibilidade.gerarRecomendacao(animal, adotante);
    }

    // Persistência
    public void salvarTodosDados() {
        try {
            repositorioAnimais.salvarDados();
            repositorioAdotantes.salvarDados();
            repositorioAdocoes.salvarDados();
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar dados: " + e.getMessage());
        }
    }
}