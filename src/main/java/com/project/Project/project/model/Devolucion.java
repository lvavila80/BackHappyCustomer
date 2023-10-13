package com.project.Project.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "venta_devolucion")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre", length = 60)
    private String nombre;

    @Column(name = "correo", length = 45)
    private String correo;

    @Column(name = "direccion", length = 45)
    private String direccion;

    @Column(name = "telefono")
    private int telefono;

    @Column(name = "cargo", length = 45)
    private String cargo;

    @Column(name = "confirmacion")
    private int confirmacion;

    // Constructor vacío
    public Devolucion() {
    }

    // Constructor con argumentos
    public Devolucion(String nombre, String correo, String direccion, int telefono, String cargo, byte confirmacion) {
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.cargo = cargo;
        this.confirmacion = confirmacion;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(int confirmacion) {
        this.confirmacion = confirmacion;
    }
}

