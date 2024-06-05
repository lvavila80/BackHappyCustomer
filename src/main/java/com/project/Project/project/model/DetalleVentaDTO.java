package com.project.Project.project.model;

import java.util.Date;

public class DetalleVentaDTO {
    private int idVenta;
    private int idArticulo;
    private int unidadesVendidas;
    private int estado;
    private double valorTotal;
    private Date fechaVenta;

    public DetalleVentaDTO(int idVenta, int idArticulo, int unidadesVendidas, int estado, double valorTotal, Date fechaVenta) {
        this.idVenta = idVenta;
        this.idArticulo = idArticulo;
        this.unidadesVendidas = unidadesVendidas;
        this.estado = estado;
        this.valorTotal = valorTotal;
        this.fechaVenta = fechaVenta;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(int unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
}
