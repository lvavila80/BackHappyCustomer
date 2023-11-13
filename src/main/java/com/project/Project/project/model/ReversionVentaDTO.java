package com.project.Project.project.model;

import jakarta.validation.constraints.*;

import java.util.ArrayList;

public class ReversionVentaDTO {

    @NotNull(message = "El ID de venta no puede ser nulo.")
    @Min(value = 1, message = "El ID de venta debe ser un número positivo.")
    private Long idVenta;

    @NotBlank(message = "El motivo de reversión no puede estar vacío.")
    private String motivoReversion;

    @NotNull(message = "La lista de artículos devueltos no puede ser nula.")
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
