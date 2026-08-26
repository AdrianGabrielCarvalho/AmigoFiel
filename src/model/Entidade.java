package model;

import java.io.Serializable;

public abstract class Entidade implements Serializable {
    protected int id;
    protected String nome;

    protected Entidade(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public abstract void validar() throws IllegalArgumentException;

    public String getInfoBasica() {
        return "ID: " + id + " | Nome: " + nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}