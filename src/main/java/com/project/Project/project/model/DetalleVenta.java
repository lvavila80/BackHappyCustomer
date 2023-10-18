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

    @ManyToOne
    @JoinColumn(name = "idventa", insertable = false, updatable = false)
    private Venta venta;


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

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}