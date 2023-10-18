package com.project.Project.project.model;

public class ArticulosCompraDTO {
    private Articulo articulo;
    private int unidadesCompradas;
    private double valorUnidad;
    private double valorTotal;
    private Integer idCategoria;

    public ArticulosCompraDTO(Articulo articulo, int unidadesCompradas, double valorUnidad, Integer idCategoria, Integer idProveedor) {
        this.articulo = articulo;
        this.unidadesCompradas = unidadesCompradas;
        this.valorUnidad = valorUnidad;
        this.idCategoria = idCategoria;
        this.valorTotal = (unidadesCompradas*valorUnidad);
    }

    public ArticulosCompraDTO() {
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public int getUnidadesCompradas() {
        return unidadesCompradas;
    }

    public void setUnidadesCompradas(int unidadesCompradas) {
        this.unidadesCompradas = unidadesCompradas;
    }

    public double getValorUnidad() {
        return valorUnidad;
    }

    public void setValorUnidad(double valorUnidad) {
        this.valorUnidad = valorUnidad;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

}
