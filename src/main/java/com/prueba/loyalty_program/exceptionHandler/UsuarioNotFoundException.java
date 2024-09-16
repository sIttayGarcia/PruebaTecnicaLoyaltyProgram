package com.prueba.loyalty_program.exceptionHandler;

public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
