package control;

import model.Adotante;
import model.RepositorioAdotantes;
import exceptions.AdotanteNaoEncontradoException;
import java.util.List;

public class AdotanteController {
    private RepositorioAdotantes repositorio;

    public AdotanteController(RepositorioAdotantes repositorio) {
        this.repositorio = repositorio;
    }

    public void cadastrarAdotante(String cpf, String nome, String telefone,
                                  model.Adotante.TipoMoradia tipoMoradia,
                                  model.Animal.Porte preferenciaPorte) {
        try {
            if (cpf == null || cpf.trim().isEmpty()) {
                throw new IllegalArgumentException("CPF é obrigatório");
            }
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome é obrigatório");
            }

            repositorio.cadastrarAdotante(cpf, nome, telefone, tipoMoradia, preferenciaPorte);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro interno no sistema", e);
        }
    }

    public Adotante buscarPorCpf(String cpf) throws AdotanteNaoEncontradoException {
        try {
            return repositorio.buscarPorCpf(cpf);
        } catch (AdotanteNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new AdotanteNaoEncontradoException("Erro interno na busca");
        }
    }

    public boolean removerAdotante(String cpf) throws AdotanteNaoEncontradoException {
        try {
            return repositorio.removerAdotante(cpf);
        } catch (AdotanteNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro interno na remoção", e);
        }
    }

    public List<Adotante> getTodosAdotantes() {
        try {
            return repositorio.getTodosAdotantes();
        } catch (Exception e) {
            System.err.println("Erro ao obter todos adotantes: " + e.getMessage());
            return List.of();
        }
    }
}