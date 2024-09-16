package com.prueba.loyalty_program.entity;

import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)  // El nombre debe tener entre 2 y 100 caracteres
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @NotNull
    @Column(name = "username", nullable = false)
    private String userName;

    @NotNull
    @Email  // Valida que el campo tenga un formato de correo electrónico
    @Column(name = "email", nullable = false, unique = true)  // El correo debe ser único
    private String email;

    @NotNull
    @Size(min = 8)  // La contraseña debe tener al menos 8 caracteres
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "saldo_puntos")
    private Integer saldoPuntos = 0;  // Saldo inicial de puntos

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();  // Fecha de registro automática
    public Usuario(){}
    public Usuario(long id, String nombre, String userName, String mail, int saldoPuntos) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSaldoPuntos() {
        return saldoPuntos;
    }

    public void setSaldoPuntos(Integer saldoPuntos) {
        this.saldoPuntos = saldoPuntos;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
