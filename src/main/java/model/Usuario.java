package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String correo;
    private String passwwd;
    private Integer cedula;
    private String nombre;
    private String estado;
    private Boolean cambiarclave;
    private String fecha_ult_cambio_clave;

    protected Usuario() {}

    public Usuario(String correo, String passwwd) {
        this.correo = correo;
        this.passwwd = passwwd;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, correo='%s', paswwd='%s']",
                id, correo, passwwd);
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return correo;
    }

    public String getPaswwd() {
        return passwwd;
    }
}