package exceptions;

public class AnimalNaoEncontradoException extends Exception {
    public AnimalNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}