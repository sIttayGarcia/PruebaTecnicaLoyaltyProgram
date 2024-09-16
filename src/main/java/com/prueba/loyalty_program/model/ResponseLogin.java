package com.prueba.loyalty_program.model;

public class ResponseLogin {

    private String message;
    private String token; // Opcional, en caso de utilizar JWT o similar

    public ResponseLogin(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
