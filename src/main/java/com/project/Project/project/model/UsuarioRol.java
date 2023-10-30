package com.project.Project.project.model;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "usuario_rol")
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public UsuarioRol() {
    }

    @Getter
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Getter
    @Column(name = "idrol")
    private Integer idRol;

    public UsuarioRol(Integer idUsuario, Integer idRol) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }
}

