package com.project.Project.project.model;


import java.util.Date;

public class UsuarioDAO {
    private int id;
    private String correo;
    private int cedula;
    private String estado;
    private boolean cambiarClave;
    private Date fechaUltimoCambioClave;

    public UsuarioDAO(int id, String correo, int cedula, String estado, boolean cambiarClave, Date fechaUltimoCambioClave) {
        this.id = id;
        this.correo = correo;
        this.cedula = cedula;
        this.estado = estado;
        this.cambiarClave = cambiarClave;
        this.fechaUltimoCambioClave = fechaUltimoCambioClave;
    }

    public UsuarioDAO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isCambiarClave() {
        return cambiarClave;
    }

    public void setCambiarClave(boolean cambiarClave) {
        this.cambiarClave = cambiarClave;
    }

    public Date getFechaUltimoCambioClave() {
        return fechaUltimoCambioClave;
    }

    public void setFechaUltimoCambioClave(Date fechaUltimoCambioClave) {
        this.fechaUltimoCambioClave = fechaUltimoCambioClave;
    }

    // Constructores, getters y setters
}