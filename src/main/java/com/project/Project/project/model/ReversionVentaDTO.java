package com.project.Project.project.model;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

public class ReversionVentaDTO {
    private Long idVenta;
    private String motivoReversion;
    @NotNull
    private ArrayList devuelto;

    public ReversionVentaDTO() {
    }

    public ReversionVentaDTO(Long idVenta, String motivoReversion, ArrayList devuelto) {
        this.idVenta = idVenta;
        this.motivoReversion = motivoReversion;
        this.devuelto = devuelto;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public String getMotivoReversion() {
        return motivoReversion;
    }

    public void setMotivoReversion(String motivoReversion) {
        this.motivoReversion = motivoReversion;
    }

    public ArrayList getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(ArrayList devuelto) {
        this.devuelto = devuelto;
    }
}
