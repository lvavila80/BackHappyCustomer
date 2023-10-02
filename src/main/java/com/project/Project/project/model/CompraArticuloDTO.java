package com.project.Project.project.model;


import com.project.Project.project.model.Articulo;
import com.project.Project.project.model.Compra;

public class CompraArticuloDTO {
    private Compra compra;
    private Articulo articulo;
    private Integer idCategoria;
    private Integer idProveedor;
    private Integer idUsuario;

    public CompraArticuloDTO(Compra compra, Articulo articulo, Integer idCategoria, Integer idProveedor, Integer idUsuario) {
        this.compra = compra;
        this.articulo = articulo;
        this.idCategoria = idCategoria;
        this.idProveedor = idProveedor;
        this.idUsuario = idUsuario;
    }

    public CompraArticuloDTO() {
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}