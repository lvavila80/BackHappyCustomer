package com.project.Project.project.model;

public class ArticuloDescontarStockDTO {

    private Integer id;
    private Integer cantidadADescontar;

    // Constructor
    public ArticuloDescontarStockDTO() {
    }

    public ArticuloDescontarStockDTO(Integer id, Integer cantidadADescontar) {
        this.id = id;
        this.cantidadADescontar = cantidadADescontar;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getCantidadADescontar() {
        return cantidadADescontar;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setCantidadADescontar(Integer cantidadADescontar) {
        this.cantidadADescontar = cantidadADescontar;
    }
}