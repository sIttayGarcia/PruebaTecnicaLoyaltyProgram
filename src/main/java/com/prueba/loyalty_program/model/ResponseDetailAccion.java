package com.prueba.loyalty_program.model;

import com.prueba.loyalty_program.entity.AccionUsuario;

public class ResponseDetailAccion {
    private Long id;
    private String userName;
    private Integer saldoPuntos;
    private String meessage;

    public ResponseDetailAccion(AccionUsuario usuario){
        this.id = usuario.getUsuario().getId();
        this.userName = usuario.getUsuario().getUserName();
        this.saldoPuntos = usuario.getUsuario().getSaldoPuntos();
        this.meessage = "Puntos acumulados correctaente";
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

    public Integer getSaldoPuntos() {
        return saldoPuntos;
    }

    public void setSaldoPuntos(Integer saldoPuntos) {
        this.saldoPuntos = saldoPuntos;
    }

    public String getMeessage() {
        return meessage;
    }

    public void setMeessage(String meessage) {
        this.meessage = meessage;
    }
}
