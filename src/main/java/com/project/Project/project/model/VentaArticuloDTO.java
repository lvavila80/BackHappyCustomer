package com.project.Project.project.model;

import java.util.List;

public class VentaArticuloDTO {
    private List<ArticuloVentaDTO> articulosVenta;
    private int idUsuario;
    private int idCliente;

    public VentaArticuloDTO(List<ArticuloVentaDTO> articulosVenta, int idUsuario, int idCliente) {
        this.articulosVenta = articulosVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
    }

    public VentaArticuloDTO() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public List<ArticuloVentaDTO> getArticulosVenta() {
        return articulosVenta;
    }

    public void setArticulosVenta(List<ArticuloVentaDTO> articulosVenta) {
        this.articulosVenta = articulosVenta;
    }
}
