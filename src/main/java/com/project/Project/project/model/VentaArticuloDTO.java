package com.project.Project.project.model;

import java.util.List;

public class VentaArticuloDTO {
    private List<ArticuloVentaDTO> articulosVenta;
    private Long idUsuario;
    private Long idCliente;
    private Long idCategoria;

    public VentaArticuloDTO(List<ArticuloVentaDTO> articulosVenta, Long idUsuario, Long idCliente, Long idCategoria) {
        this.articulosVenta = articulosVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.idCategoria = idCategoria;
    }

    public VentaArticuloDTO() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public List<ArticuloVentaDTO> getArticulosVenta() {
        return articulosVenta;
    }

    public void setArticulosVenta(List<ArticuloVentaDTO> articulosVenta) {
        this.articulosVenta = articulosVenta;
    }
}
