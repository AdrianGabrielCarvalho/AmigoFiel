package model;

public class Adotante extends Entidade {
    public enum TipoMoradia { CASA, APARTAMENTO }

    private String cpf;
    private String telefone;
    private TipoMoradia tipoMoradia;
    private Animal.Porte preferenciaPorte;

    public Adotante(String cpf, String nome, String telefone,
                    TipoMoradia tipoMoradia, Animal.Porte preferenciaPorte) {
        super(0, nome);
        this.cpf = cpf;
        this.telefone = telefone;
        this.tipoMoradia = tipoMoradia;
        this.preferenciaPorte = preferenciaPorte;
    }

    @Override
    public void validar() throws IllegalArgumentException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do adotante e obrigatorio");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF e obrigatorio");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone e obrigatorio");
        }
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoMoradia getTipoMoradia() {
        return tipoMoradia;
    }

    public void setTipoMoradia(TipoMoradia tipoMoradia) {
        this.tipoMoradia = tipoMoradia;
    }

    public Animal.Porte getPreferenciaPorte() {
        return preferenciaPorte;
    }

    public void setPreferenciaPorte(Animal.Porte preferenciaPorte) {
        this.preferenciaPorte = preferenciaPorte;
    }

    @Override
    public String toString() {
        String moradiaDesc = (tipoMoradia == TipoMoradia.CASA) ? "CASA" : "APARTAMENTO";
        String porteDesc = "";

        if (preferenciaPorte != null) {
            switch(preferenciaPorte) {
                case PEQUENO: porteDesc = "PEQUENO"; break;
                case MEDIO: porteDesc = "MEDIO"; break;
                case GRANDE: porteDesc = "GRANDE"; break;
            }
        } else {
            porteDesc = "INDIFERENTE";
        }

        return String.format("%s | Moradia: %s | Prefere: %s | Tel: %s",
                nome, moradiaDesc, porteDesc, telefone);
    }
}