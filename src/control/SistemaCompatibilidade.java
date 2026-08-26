package control;

import model.Animal;
import model.Adotante;

public class SistemaCompatibilidade {

    public static boolean verificarCompatibilidade(Animal animal, Adotante adotante) {
        boolean porteCompativel = verificarPorte(animal.getPorte(), adotante);
        boolean temperamentoCompativel = verificarTemperamento(animal.getTemperamento(), adotante);

        return porteCompativel && temperamentoCompativel;
    }

    private static boolean verificarPorte(Animal.Porte porteAnimal, Adotante adotante) {
        Animal.Porte preferencia = adotante.getPreferenciaPorte();

        if (adotante.getTipoMoradia() == Adotante.TipoMoradia.APARTAMENTO) {
            return porteAnimal == Animal.Porte.PEQUENO || porteAnimal == Animal.Porte.MEDIO;
        }

        return preferencia == null || porteAnimal == preferencia;
    }

    private static boolean verificarTemperamento(Animal.Temperamento temperamento, Adotante adotante) {
        if (adotante.getTipoMoradia() == Adotante.TipoMoradia.APARTAMENTO &&
                temperamento == Animal.Temperamento.AGITADO) {
            return false;
        }

        return true;
    }

    public static String gerarRecomendacao(Animal animal, Adotante adotante) {
        StringBuilder recomendacao = new StringBuilder();

        if (verificarCompatibilidade(animal, adotante)) {
            recomendacao.append("Compatibilidade alta!\n");
            recomendacao.append("Este animal é ideal para seu perfil.\n");
        } else {
            recomendacao.append("Compatibilidade baixa\n");
            recomendacao.append("Recomendamos considerar outro animal.\n");
        }

        if (animal.getPorte() == Animal.Porte.GRANDE &&
                adotante.getTipoMoradia() == Adotante.TipoMoradia.APARTAMENTO) {
            recomendacao.append("Obs: Animais grandes precisam de mais espaço.\n");
        }

        if (animal.getTemperamento() == Animal.Temperamento.AGITADO) {
            recomendacao.append("Obs: Este animal tem alta energia e precisa de exercícios.\n");
        }

        return recomendacao.toString();
    }
}