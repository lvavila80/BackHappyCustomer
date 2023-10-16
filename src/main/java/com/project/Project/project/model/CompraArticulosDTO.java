package com.project.Project.project.model;
import java.util.List;

public class CompraArticulosDTO {
    private List<ArticulosCompraDTO> articulosCompra;  // Lista de ArticuloCompraDTO
    private Integer idUsuario;

    public CompraArticulosDTO(List<ArticulosCompraDTO> articulosCompra, Integer idUsuario) {
        this.articulosCompra = articulosCompra;
        this.idUsuario = idUsuario;
    }

    public CompraArticulosDTO() {
    }

    public List<ArticulosCompraDTO> getArticulosCompra() {
        return articulosCompra;
    }

    public void setArticulosCompra(List<ArticulosCompraDTO> articulosCompra) {
        this.articulosCompra = articulosCompra;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}