package com.project.Project.project.model;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "usuario_rol")
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public UsuarioRol() {
    }

    @Getter
    @Column(name = "idusuario")
    private int idUsuario;

    @Getter
    @Column(name = "idrol")
    private int idRol;

    public UsuarioRol(int idUsuario, int idRol) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}

