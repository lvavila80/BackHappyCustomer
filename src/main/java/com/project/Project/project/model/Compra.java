package com.project.Project.project.model;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "compras")
public class Compra {

    @Column(name = "descripcion_devolucion", length = 200)
    private String descripcionDevolucion;

    @Column(name = "devuelto")
    private Boolean devuelto;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private double valortotal;

    public Compra() {
        this.fechacompra = new Date();
    }

    public Compra(double valortotal) {
        this.valortotal = valortotal;
        this.fechacompra = new Date();
    }

    public String getDescripcionDevolucion() {
        return descripcionDevolucion;
    }

    public Boolean getDevuelto() {
        return devuelto;
    }

    public void setDescripcionDevolucion(String descripcionDevolucion) {
        this.descripcionDevolucion = descripcionDevolucion;
    }

    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}