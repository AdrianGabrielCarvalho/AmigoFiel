package model;

public class Animal extends Entidade {
    public enum Especie { CAO, GATO }
    public enum Porte { PEQUENO, MEDIO, GRANDE }
    public enum Temperamento { DOCIL, AGITADO, CALMO, BRINCALHAO }
    public enum Status { DISPONIVEL, ADOTADO, EM_PROCESSO }

    private Especie especie;
    private int idade;
    private Porte porte;
    private Temperamento temperamento;
    private Status status;

    public Animal(int id, String nome, Especie especie, int idade,
                  Porte porte, Temperamento temperamento) {
        super(id, nome);
        this.especie = especie;
        this.idade = idade;
        this.porte = porte;
        this.temperamento = temperamento;
        this.status = Status.DISPONIVEL;
    }

    @Override
    public void validar() throws IllegalArgumentException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do animal é obrigatório");
        }
        if (idade < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa");
        }
        if (idade > 30) {
            throw new IllegalArgumentException("Idade máxima é 30 anos");
        }
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public Temperamento getTemperamento() {
        return temperamento;
    }

    public void setTemperamento(Temperamento temperamento) {
        this.temperamento = temperamento;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String especieTexto = (especie == Especie.CAO) ? "Cão" : "Gato";
        String statusTexto = "";
        switch(status) {
            case DISPONIVEL: statusTexto = "Disponível"; break;
            case ADOTADO: statusTexto = "Adotado"; break;
            case EM_PROCESSO: statusTexto = "Em processo"; break;
        }

        return String.format("%s %s | Idade: %d anos | Porte: %s | Temperamento: %s",
                statusTexto, nome, idade, porte, temperamento);
    }
}