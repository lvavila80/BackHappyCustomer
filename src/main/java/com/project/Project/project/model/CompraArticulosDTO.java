package com.project.Project.project.model;
import java.util.List;

public class CompraArticulosDTO {
    private List<ArticulosCompraDTO> articulosCompra;
    private Integer idUsuario;
    private Integer idProveedor;


    public CompraArticulosDTO(List<ArticulosCompraDTO> articulosCompra, Integer idUsuario, Integer idProveedor) {
        this.articulosCompra = articulosCompra;
        this.idUsuario = idUsuario;
        this.idProveedor = idProveedor;
    }

    public CompraArticulosDTO() {
    }
    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
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