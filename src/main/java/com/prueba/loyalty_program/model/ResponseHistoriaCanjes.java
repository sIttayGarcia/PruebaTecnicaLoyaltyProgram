package com.prueba.loyalty_program.model;

import com.prueba.loyalty_program.entity.Canje;

import java.time.LocalDateTime;

public class ResponseHistoriaCanjes {

    private Long id;
    private String username;

    private String nombreRecompensa;

    private LocalDateTime fechaCanje;

    public ResponseHistoriaCanjes(Canje canje) {
        this.id = canje.getId();
        this.username = (canje.getUsuario() != null) ? canje.getUsuario().getUserName() : "Usuario no disponible";
        this.nombreRecompensa = (canje.getRecompensa() != null) ? canje.getRecompensa().getNombre() : "Recompensa no disponible";
        this.fechaCanje = canje.getFechaCanje();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreRecompensa() {
        return nombreRecompensa;
    }

    public void setNombreRecompensa(String nombreRecompensa) {
        this.nombreRecompensa = nombreRecompensa;
    }

    public LocalDateTime getFechaCanje() {
        return fechaCanje;
    }

    public void setFechaCanje(LocalDateTime fechaCanje) {
        this.fechaCanje = fechaCanje;
    }
}
