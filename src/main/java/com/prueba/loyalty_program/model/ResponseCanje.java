package com.prueba.loyalty_program.model;

import com.prueba.loyalty_program.entity.Recompensa;
import com.prueba.loyalty_program.entity.Usuario;

public class ResponseCanje {

    private String userName;
    private String recompensaNombre;
    private Integer saldoRestante;

    private String message;
    public ResponseCanje() {
    }
    public ResponseCanje(Usuario usuario, Recompensa recompensa){
        this.userName = usuario.getUserName();
        this.recompensaNombre = recompensa.getNombre();
        this.saldoRestante = usuario.getSaldoPuntos();
        this.message = getMessage();

    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecompensaNombre() {
        return recompensaNombre;
    }

    public void setRecompensaNombre(String recompensaNombre) {
        this.recompensaNombre = recompensaNombre;
    }

    public Integer getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(Integer saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
