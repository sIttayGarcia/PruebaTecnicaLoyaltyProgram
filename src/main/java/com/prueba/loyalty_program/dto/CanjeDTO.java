package com.prueba.loyalty_program.dto;

public class CanjeDTO {
    private String username;

    private Long recompensaId;

    private Integer cantidad;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRecompensaId() {
        return recompensaId;
    }

    public void setRecompensaId(Long recompensaId) {
        this.recompensaId = recompensaId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
