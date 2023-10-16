package com.project.Project.project.model;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer unidadescompradas;
    private double valorunidad;
    private double valortotal;

    public Compra() {
        this.fechacompra = new Date();
    }

    @Column(name = "devuelto")
    private Boolean devuelto = false;

    @Column(name = "descripcion_devolucion", length = 200)
    private String descripcionDevolucion;

    public Compra(Integer unidadescompradas, double valorunidad) {
        this.unidadescompradas = unidadescompradas;
        this.valorunidad = valorunidad;
        this.valortotal = (unidadescompradas*valorunidad);
        this.fechacompra = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUnidadescompradas() {
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

    private Date fechacompra;

    public Boolean getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
    }

    public String getDescripcionDevolucion() {
        return descripcionDevolucion;
    }

    public void setDescripcionDevolucion(String descripcionDevolucion) {
        this.descripcionDevolucion = descripcionDevolucion;
    }


}