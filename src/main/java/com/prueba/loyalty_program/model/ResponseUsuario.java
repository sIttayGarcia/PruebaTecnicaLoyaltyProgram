package com.prueba.loyalty_program.model;

import com.prueba.loyalty_program.entity.Usuario;

public class ResponseUsuario {

    private Long id;

    private String userName;

    private String message;

    public ResponseUsuario(Usuario usuario) {
        this.id = usuario.getId();
        this.userName = usuario.getUserName();
        this.message = "Usuario creado correctamente";
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
