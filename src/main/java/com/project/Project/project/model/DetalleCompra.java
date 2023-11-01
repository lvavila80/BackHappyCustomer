package com.project.Project.project.model;

import jakarta.persistence.*;

@Entity

@Table(name = "detalle_compra")

public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "idcompra", length = 45)
    private int idcompra;
    @Column(name = "idcategoria", length = 45)
    private double idcategoria;
    @Column(name = "idarticulo", length = 45)
    private int idarticulo;
    @Column(name = "unidadescompradas", length = 45)
    private int unidadescompradas;
    @Column(name = "valorunidad", length = 45)
    private double valorunidad;

    @Column(name = "estado", length = 45)
    private String estado;

    @Column(name = "detalle_devolucion", length = 45)
    private String detalleDevolucion;


    public DetalleCompra(int idcompra, int idarticulo, int idcategoria, int unidadescompradas, double valorunidad, String estado, String detalleDevolucion) {
        this.idcompra = idcompra;
        this.idarticulo = idarticulo;
        this.idcategoria = idcategoria;
        this.unidadescompradas = unidadescompradas;
        this.valorunidad = valorunidad;
        this.estado = estado;
        this.detalleDevolucion = detalleDevolucion;
    }

    public DetalleCompra(int idcompra, int idarticulo, int unidadescompradas, double valorunidad, String estado, String detalleDevolucion) {
        this.idcompra = idcompra;
        this.idarticulo = idarticulo;
        this.unidadescompradas = unidadescompradas;
        this.valorunidad = valorunidad;
        this.estado = estado;
        this.detalleDevolucion = detalleDevolucion;
    }

    public DetalleCompra() {
    }

    public String getDetalleDevolucion() {
        return detalleDevolucion;
    }

    public void setDetalleDevolucion(String detalleDevolucion) {
        this.detalleDevolucion = detalleDevolucion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(int idcompra) {
        this.idcompra = idcompra;
    }

    public int getIdarticulo() {
        return idarticulo;
    }

    public void setIdarticulo(int idarticulo) {
        this.idarticulo = idarticulo;
    }

    public int getUnidadescompradas() {
        return unidadescompradas;
    }

    public void setUnidadescompradas(int unidadescompradas) {
        this.unidadescompradas = unidadescompradas;
    }

    public double getValorunidad() {
        return valorunidad;
    }

    public double getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(double idcategoria) {
        this.idcategoria = idcategoria;
    }

    public void setValorunidad(double valorunidad) {
        this.valorunidad = valorunidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
