package com.project.Project.project.model;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    private Long id;
    private int identificacion;
    private String nombre;

    // Getters y Setters (o puedes generarlos automáticamente según tu entorno de desarrollo)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
