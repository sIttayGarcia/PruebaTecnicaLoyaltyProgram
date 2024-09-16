package com.prueba.loyalty_program.model;

import com.prueba.loyalty_program.entity.Usuario;

import java.time.LocalDateTime;

public class ResponseUsuarioDetail {

    private Long id;

    private String userName;

    private Integer saldoActual;

    private LocalDateTime fechaRegistro;
    private String message;

    public ResponseUsuarioDetail(Usuario usuario) {
        this.id = usuario.getId();
        this.userName = usuario.getUserName();
        this.saldoActual = usuario.getSaldoPuntos();
        this.fechaRegistro = usuario.getFechaRegistro();
        this.message = "Consulta realizada correctamente";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Integer saldoActual) {
        this.saldoActual = saldoActual;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
