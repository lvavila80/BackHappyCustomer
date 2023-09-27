package com.project.Project.project.model;

import jakarta.persistence.*;


@Entity
@Table(name = "categorias")

public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombreCategorias", length = 45)
    private String nombreCategorias;

    // Constructor
    public Categoria() {
    }

    public Categoria(int id, String nombreCategorias) {
        this.id = id;
        this.nombreCategorias = nombreCategorias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCategorias() {
        return nombreCategorias;
    }

    public void setNombreCategorias(String nombreCategorias) {
        this.nombreCategorias = nombreCategorias;
    }
}
