package model;

import persistence.GerenciadorArquivos;
import exceptions.AdotanteNaoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAdotantes {
    private List<Adotante> adotantes;

    public RepositorioAdotantes() {
        this.adotantes = GerenciadorArquivos.carregarAdotantes();
    }

    public void cadastrarAdotante(String cpf, String nome, String telefone,
                                  Adotante.TipoMoradia tipoMoradia, Animal.Porte preferenciaPorte) {
        try {
            Adotante adotante = new Adotante(cpf, nome, telefone, tipoMoradia, preferenciaPorte);
            adotante.validar();
            adotantes.add(adotante);
            salvarDados();
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao cadastrar adotante: " + e.getMessage());
        }
    }

    public Adotante buscarPorCpf(String cpf) throws AdotanteNaoEncontradoException {
        Adotante adotante = adotantes.stream()
                .filter(a -> a.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);

        if (adotante == null) {
            throw new AdotanteNaoEncontradoException("Adotante com CPF " + cpf + " não encontrado");
        }
        return adotante;
    }

    public boolean removerAdotante(String cpf) throws AdotanteNaoEncontradoException {
        Adotante adotante = buscarPorCpf(cpf);
        if (adotante != null) {
            adotantes.remove(adotante);
            salvarDados();
            return true;
        }
        return false;
    }

    public List<Adotante> getTodosAdotantes() {
        return new ArrayList<>(adotantes);
    }

    public void salvarDados() {
        try {
            GerenciadorArquivos.salvarAdotantes(adotantes);
        } catch (Exception e) {
            System.err.println("Erro ao salvar adotantes: " + e.getMessage());
        }
    }
}