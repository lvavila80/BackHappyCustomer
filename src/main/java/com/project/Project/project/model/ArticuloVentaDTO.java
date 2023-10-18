package com.project.Project.project.model;

public class ArticuloVentaDTO {
    private int articulo;
    private int unidadesVendidas;
    private int idCategoria;

    public ArticuloVentaDTO(int articulo, int unidadesVendidas, int idCategoria) {
        this.articulo = articulo;
        this.unidadesVendidas = unidadesVendidas;
        this.idCategoria = idCategoria;
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

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
