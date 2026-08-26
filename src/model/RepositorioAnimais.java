package model;

import persistence.GerenciadorArquivos;
import exceptions.AnimalNaoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioAnimais {
    private List<Animal> animais;
    private int proximoId;

    public RepositorioAnimais() {
        this.animais = GerenciadorArquivos.carregarAnimais();
        calcularProximoId();
    }

    private void calcularProximoId() {
        this.proximoId = animais.stream()
                .mapToInt(Animal::getId)
                .max()
                .orElse(0) + 1;
    }

    public Animal cadastrarAnimal(String nome, Animal.Especie especie, int idade,
                                  Animal.Porte porte, Animal.Temperamento temperamento) {
        try {
            Animal animal = new Animal(proximoId++, nome, especie, idade, porte, temperamento);
            animal.validar();
            animais.add(animal);
            salvarDados();
            return animal;
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao cadastrar animal: " + e.getMessage());
            return null;
        }
    }

    public List<Animal> listarDisponiveis() {
        return animais.stream()
                .filter(a -> a.getStatus() == Animal.Status.DISPONIVEL)
                .collect(Collectors.toList());
    }

    public List<Animal> filtrarPorEspecie(Animal.Especie especie) {
        return animais.stream()
                .filter(a -> a.getEspecie() == especie)
                .collect(Collectors.toList());
    }

    public Animal buscarPorId(int id) throws AnimalNaoEncontradoException {
        Animal animal = animais.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);

        if (animal == null) {
            throw new AnimalNaoEncontradoException("Animal com ID " + id + " não encontrado");
        }
        return animal;
    }

    public boolean atualizarAnimal(Animal animal) {
        try {
            animal.validar();
        } catch (IllegalArgumentException e) {
            System.err.println("Animal inválido: " + e.getMessage());
            return false;
        }

        for (int i = 0; i < animais.size(); i++) {
            if (animais.get(i).getId() == animal.getId()) {
                animais.set(i, animal);
                salvarDados();
                return true;
            }
        }
        return false;
    }

    public boolean removerAnimal(int id) throws AnimalNaoEncontradoException {
        Animal animal = buscarPorId(id);
        if (animal != null) {
            animais.remove(animal);
            salvarDados();
            return true;
        }
        return false;
    }

    public List<Animal> getTodosAnimais() {
        return new ArrayList<>(animais);
    }

    public void salvarDados() {
        try {
            GerenciadorArquivos.salvarAnimais(animais);
        } catch (Exception e) {
            System.err.println("Erro ao salvar animais: " + e.getMessage());
        }
    }

    public int getProximoId() {
        return proximoId;
    }
}