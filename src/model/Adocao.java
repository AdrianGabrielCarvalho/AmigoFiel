package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Adocao implements Serializable {
    private String protocolo;
    private Animal animalSelecionado;
    private Adotante adotanteResponsavel;
    private LocalDate data;
    private boolean concluida;

    public Adocao(Animal animal, Adotante adotante) {
        this.protocolo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.animalSelecionado = animal;
        this.adotanteResponsavel = adotante;
        this.data = LocalDate.now();
        this.concluida = false;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public Animal getAnimalSelecionado() {
        return animalSelecionado;
    }

    public void setAnimalSelecionado(Animal animalSelecionado) {
        this.animalSelecionado = animalSelecionado;
    }

    public Adotante getAdotanteResponsavel() {
        return adotanteResponsavel;
    }

    public void setAdotanteResponsavel(Adotante adotanteResponsavel) {
        this.adotanteResponsavel = adotanteResponsavel;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
        if (concluida) {
            animalSelecionado.setStatus(Animal.Status.ADOTADO);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String status = concluida ? "[CONCLUIDA]" : "[PENDENTE]";

        return String.format("%s Protocolo: %s | Data: %s | Animal: %s | Adotante: %s",
                status, protocolo, data.format(formatter),
                animalSelecionado.getNome(), adotanteResponsavel.getNome());
    }
}