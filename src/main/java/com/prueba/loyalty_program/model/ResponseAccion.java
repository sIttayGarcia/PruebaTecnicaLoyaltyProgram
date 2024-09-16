package com.prueba.loyalty_program.model;

import com.prueba.loyalty_program.entity.Accion;

public class ResponseAccion {

    private Long id;

    private String actionName;
    private String message;

    public ResponseAccion(Accion accion){
        this.id = accion.getId();
        this.actionName = accion.getNombre();
        this.message = "Acción creada correctamente";
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
