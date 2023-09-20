package com.project.Project.project.model;

/**import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.Date;

@Entity
**/
public class Usuario {
/**    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "correo", length = 45, nullable = false)
    private String correo;

    @Column(name = "passwd", length = 45, nullable = false)
    private String passwd;

    @Column(name = "cedula", length = 11)
    private int cedula;

    @Column(name = "estado", length = 45)
    private String estado;

    @Column(name = "cambiarclave")
    private boolean cambiarClave;

    @Column(name = "fecha_ult_cambio_clave")
    private Date fechaUltimoCambioClave;

    // Constructor, getters y setters

    public Usuario() {
    }

    public Usuario(String correo, String passwd, int cedula, String estado, boolean cambiarClave, Date fechaUltimoCambioClave) {
        this.correo = correo;
        this.passwd = passwd;
        this.cedula = cedula;
        this.estado = estado;
        this.cambiarClave = cambiarClave;
        this.fechaUltimoCambioClave = fechaUltimoCambioClave;
    }

    // Getters y setters

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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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
**/
}