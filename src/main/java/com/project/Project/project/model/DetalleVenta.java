package com.project.Project.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "idventa", nullable = false)
    private int idventa;

    @Column(name = "idarticulo", nullable = false)
    private int idarticulo;

    @Column(name = "unidadesvendidas", nullable = false)
    private int unidadesvendidas;

    @Column(name = "valorunidad", nullable = false)
    private Double valorunidad;

    @Column(name = "idcategoria", nullable = false)
    private int idcategoria;

    @Column(name = "estado", length = 45)
    private String estado;
    @Column(name = "detalle_devolucion", length = 45)
    private String detalleDevolucion;

    public DetalleVenta() {
    }

    public DetalleVenta(int id, int idventa, int idarticulo, int unidadesvendidas, Double valorunidad, int idcategoria, String estado, String detalleDevolucion, Venta venta) {
        this.id = id;
        this.idventa = idventa;
        this.idarticulo = idarticulo;
        this.unidadesvendidas = unidadesvendidas;
        this.valorunidad = valorunidad;
        this.idcategoria = idcategoria;
        this.estado = estado;
        this.detalleDevolucion = detalleDevolucion;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalleDevolucion() {
        return detalleDevolucion;
    }

    public void setDetalleDevolucion(String detalleDevolucion) {
        this.detalleDevolucion = detalleDevolucion;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdventa() {
        return idventa;
    }
    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }
    public int getIdarticulo() {
        return idarticulo;
    }
    public void setIdarticulo(int idarticulo) {
        this.idarticulo = idarticulo;
    }
    public int getUnidadesvendidas() {
        return unidadesvendidas;
    }
    public void setUnidadesvendidas(int unidadesvendidas) {
        this.unidadesvendidas = unidadesvendidas;
    }
    public Double getValorunidad() {
        return valorunidad;
    }
    public void setValorunidad(Double valorunidad) {
        this.valorunidad = valorunidad;
    }
    public int getIdcategoria() {
        return idcategoria;
    }
    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }
}