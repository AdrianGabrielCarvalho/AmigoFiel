package control;

import model.Animal;
import model.RepositorioAnimais;
import exceptions.AnimalNaoEncontradoException;
import java.util.List;

public class AnimalController {
    private RepositorioAnimais repositorio;

    public AnimalController(RepositorioAnimais repositorio) {
        this.repositorio = repositorio;
    }

    public Animal cadastrarAnimal(String nome, Animal.Especie especie, int idade,
                                  Animal.Porte porte, Animal.Temperamento temperamento) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome é obrigatório");
            }
            if (idade < 0) {
                throw new IllegalArgumentException("Idade não pode ser negativa");
            }

            return repositorio.cadastrarAnimal(nome, especie, idade, porte, temperamento);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro interno no sistema", e);
        }
    }

    public List<Animal> listarDisponiveis() {
        try {
            return repositorio.listarDisponiveis();
        } catch (Exception e) {
            System.err.println("Erro ao listar animais disponíveis: " + e.getMessage());
            return List.of();
        }
    }

    public List<Animal> filtrarPorEspecie(Animal.Especie especie) {
        try {
            return repositorio.filtrarPorEspecie(especie);
        } catch (Exception e) {
            System.err.println("Erro ao filtrar animais: " + e.getMessage());
            return List.of();
        }
    }

    public Animal buscarPorId(int id) throws AnimalNaoEncontradoException {
        try {
            return repositorio.buscarPorId(id);
        } catch (AnimalNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new AnimalNaoEncontradoException("Erro interno na busca");
        }
    }

    public boolean atualizarStatus(int id, Animal.Status status) throws AnimalNaoEncontradoException {
        try {
            Animal animal = repositorio.buscarPorId(id);
            animal.setStatus(status);
            return repositorio.atualizarAnimal(animal);
        } catch (AnimalNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar status: " + e.getMessage());
            return false;
        }
    }

    public boolean removerAnimal(int id) throws AnimalNaoEncontradoException, IllegalStateException {
        try {
            Animal animal = repositorio.buscarPorId(id);

            if (animal.getStatus() == Animal.Status.ADOTADO) {
                throw new IllegalStateException("Não pode remover animal já adotado");
            }

            return repositorio.removerAnimal(id);
        } catch (AnimalNaoEncontradoException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro interno na remoção", e);
        }
    }

    public List<Animal> getTodosAnimais() {
        try {
            return repositorio.getTodosAnimais();
        } catch (Exception e) {
            System.err.println("Erro ao obter todos animais: " + e.getMessage());
            return List.of();
        }
    }
}