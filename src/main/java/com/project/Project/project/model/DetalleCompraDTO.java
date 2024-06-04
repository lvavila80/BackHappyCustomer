package com.project.Project.project.dto;

import java.util.Date;

public class DetalleCompraDTO {
    private Integer id;
    private int idcompra;
    private int idarticulo;
    private String nombreArticulo;
    private int unidadescompradas;
    private double valorunidad;
    private Integer estado;
    private String detalleDevolucion;
    private double valortotal;
    private Date fechacompra;

    public DetalleCompraDTO() {
    }

    public DetalleCompraDTO(Integer id, int idcompra, int idarticulo, String nombreArticulo, int unidadescompradas, double valorunidad, Integer estado, String detalleDevolucion, double valortotal, Date fechacompra) {
        this.id = id;
        this.idcompra = idcompra;
        this.idarticulo = idarticulo;
        this.nombreArticulo = nombreArticulo;
        this.unidadescompradas = unidadescompradas;
        this.valorunidad = valorunidad;
        this.estado = estado;
        this.detalleDevolucion = detalleDevolucion;
        this.valortotal = valortotal;
        this.fechacompra = fechacompra;
    }

    // Getters and setters

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

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
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

    public void setValorunidad(double valorunidad) {
        this.valorunidad = valorunidad;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getDetalleDevolucion() {
        return detalleDevolucion;
    }

    public void setDetalleDevolucion(String detalleDevolucion) {
        this.detalleDevolucion = detalleDevolucion;
    }

    public double getValortotal() {
        return valortotal;
    }

    public void setValortotal(double valortotal) {
        this.valortotal = valortotal;
    }

    public Date getFechacompra() {
        return fechacompra;
    }

    public void setFechacompra(Date fechacompra) {
        this.fechacompra = fechacompra;
    }
}

