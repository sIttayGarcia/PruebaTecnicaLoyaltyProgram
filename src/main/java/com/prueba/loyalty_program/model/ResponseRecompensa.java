package com.prueba.loyalty_program.model;

import com.prueba.loyalty_program.entity.Recompensa;

public class ResponseRecompensa {

    private Long id;

    private String nombre;
    private String message;

    public ResponseRecompensa(Recompensa recompensa){
        this.id = recompensa.getId();
        this.nombre = recompensa.getNombre();
        this.message = "Recompensa creada";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
