package com.project.Project.project.model;

public class ArticuloVentaDTO {
    private Articulo articulo;
    private int unidadesVendidas;
    private double valorUnidad;
    private double valorTotal;
    private Integer idCategoria;

    public ArticuloVentaDTO(Articulo articulo, int unidadesVendidas, double valorUnidad, Integer idCategoria) {
        this.articulo = articulo;
        this.unidadesVendidas = unidadesVendidas;
        this.valorUnidad = valorUnidad;
        this.idCategoria = idCategoria;
        this.valorTotal = unidadesVendidas * valorUnidad;
    }

    public ArticuloVentaDTO() {
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

    public int getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(int unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
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
