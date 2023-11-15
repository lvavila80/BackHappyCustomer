package com.project.Project.project.model;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class ReversionVentaDTO {

    @NotNull(message = "El ID de venta no puede ser nulo.")
    @Min(value = 1, message = "El ID de venta debe ser un número positivo.")
    private int idVenta;

    @NotBlank(message = "El motivo de reversión no puede estar vacío.")
    private String motivoReversion;

    @NotNull(message = "La lista de artículos devueltos no puede ser nula.")
    private List<ProductoRevertidoDTO> devuelto;

    @NotNull(message = "La confirmación del usuario es requerida.")
    private boolean confirmacionUsuario;

    public ReversionVentaDTO() {
    }

    public ReversionVentaDTO(int idVenta, String motivoReversion, List<ProductoRevertidoDTO> devuelto, boolean confirmacionUsuario) {
        this.idVenta = idVenta;
        this.motivoReversion = motivoReversion;
        this.devuelto = devuelto;
        this.confirmacionUsuario = confirmacionUsuario;
    }


    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getMotivoReversion() {
        return motivoReversion;
    }

    public void setMotivoReversion(String motivoReversion) {
        this.motivoReversion = motivoReversion;
    }

    public ArrayList getDevuelto() {
        return (ArrayList) devuelto;
    }

    public void setDevuelto(List<ProductoRevertidoDTO> devuelto) {
        this.devuelto = devuelto;
    }

    public boolean isConfirmacionUsuario() {
        return confirmacionUsuario;
    }

    public void setConfirmacionUsuario(boolean confirmacionUsuario) {
        this.confirmacionUsuario = confirmacionUsuario;
    }
}

