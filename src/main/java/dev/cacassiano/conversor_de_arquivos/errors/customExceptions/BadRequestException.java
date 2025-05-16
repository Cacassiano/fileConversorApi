package dev.cacassiano.conversor_de_arquivos.errors.customExceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
