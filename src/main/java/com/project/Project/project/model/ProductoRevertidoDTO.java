package com.project.Project.project.model;

public class ProductoRevertidoDTO {
    private int idArticulo;
    private int cantidad;

    public ProductoRevertidoDTO() {
    }

    public ProductoRevertidoDTO(int idArticulo, int cantidad) {
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
