package com.project.Project.project.model;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public class DevoUpdateDTO {
    private Integer idCompra;
    private String descripcion;
    @NotNull
    private ArrayList devuelto;

    public DevoUpdateDTO() {
    }

    public DevoUpdateDTO(Integer idCompra, String descripcion, ArrayList devuelto) {
        this.idCompra = idCompra;
        this.descripcion = descripcion;
        this.devuelto = devuelto;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(ArrayList devuelto) {
        this.devuelto = devuelto;
    }
}
