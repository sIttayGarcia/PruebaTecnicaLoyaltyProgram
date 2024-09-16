package com.prueba.loyalty_program.exceptionHandler;

public class AccionNotFoundException extends RuntimeException{
    public AccionNotFoundException(String message) {
        super(message);
    }
}
