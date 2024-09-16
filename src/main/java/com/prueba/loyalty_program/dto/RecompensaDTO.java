package com.prueba.loyalty_program.dto;

public class RecompensaDTO {

    private String nombre;

    private String descripcion;
    private Integer puntosNecesarios;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPuntosNecesarios() {
        return puntosNecesarios;
    }

    public void setPuntosNecesarios(Integer puntosNecesarios) {
        this.puntosNecesarios = puntosNecesarios;
    }
}
