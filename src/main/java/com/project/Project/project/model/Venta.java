package com.project.Project.project.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ventas")
public class Venta {
    public Venta() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Venta(int id, double valorTotal, Date fechaVenta) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.fechaVenta = fechaVenta;
    }

    @Column(name = "valortotal")
    private double valorTotal;

    @Column(name = "fechaventa")
    private Date fechaVenta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
