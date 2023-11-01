package com.project.Project.project.model;

public class ArticuloVentaDTO {
    private int articulo;
    private int unidadesVendidas;

    public ArticuloVentaDTO() {
    }

    public ArticuloVentaDTO(int articulo, int unidadesVendidas) {
        this.articulo = articulo;
        this.unidadesVendidas = unidadesVendidas;
    }

    public int getArticulo() {
        return articulo;
    }

    public void setArticulo(int articulo) {
        this.articulo = articulo;
    }

    public int getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(int unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
    }

}
