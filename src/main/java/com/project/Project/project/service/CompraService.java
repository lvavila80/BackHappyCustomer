package com.project.Project.project.service;
import com.project.Project.project.model.*;
import com.project.Project.project.model.ArticulosCompraDTO;
import com.project.Project.project.repository.CompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ArticuloService articuloService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void guardarCompraYRelaciones(CompraArticulosDTO compraArticulosDTO) {
        for(ArticulosCompraDTO articuloCompra : compraArticulosDTO.getArticulosCompra()) {
            try {
                Compra compra = new Compra();
                compra.setUnidadescompradas(articuloCompra.getUnidadesCompradas());
                compra.setValorunidad(articuloCompra.getValorUnidad());
                Integer idArticulo = articuloService.guardarArticulo(articuloCompra);
                Compra savedCompra = guardarCompra(articuloCompra);

                if (savedCompra == null || savedCompra.getId() == null) {
                    throw new RuntimeException("Error al guardar la Compra");
                }

                Integer idCompra = savedCompra.getId();

                try {
                    compraRepository.insertCompraArticulo(idArticulo, idCompra);
                } catch (Exception e) {
                    throw new RuntimeException("Error al insertar en bd.compra_articulo: " + e.getMessage());
                }

                try {
                    compraRepository.insertCompraCategoria(idCompra, articuloCompra.getIdCategoria());
                } catch (Exception e) {
                    throw new RuntimeException("Error al insertar en bd.compra_categoria: " + e.getMessage());
                }

                try {
                    compraRepository.insertCompraProveedor(articuloCompra.getIdProveedor(), idCompra);
                } catch (Exception e) {
                    throw new RuntimeException("Error al insertar en bd.compra_proveedor: " + e.getMessage());
                }

                try {
                    compraRepository.insertCompraUsuario(idCompra, compraArticulosDTO.getIdUsuario());
                } catch (Exception e) {
                    throw new RuntimeException("Error al insertar en bd.compra_usuario: " + e.getMessage());
                }

                try {
                    compraRepository.insertArticuloCategoria(idArticulo, articuloCompra.getIdCategoria());
                } catch (Exception e) {
                    throw new RuntimeException("Error al insertar en bd.articulo_categoria: " + e.getMessage());
                }

            } catch (Exception e) {
                throw new RuntimeException("Error al guardar compra y relaciones: " + e.getMessage(), e);
            }
        }
    }
    public Compra guardarCompra(ArticulosCompraDTO ArtCompra) {
        try {
            Compra compra = new Compra();
            compra.setUnidadescompradas(ArtCompra.getUnidadesCompradas());
            compra.setValorunidad(ArtCompra.getValorUnidad());
            compra.setValortotal(ArtCompra.getValorUnidad()*ArtCompra.getUnidadesCompradas());
            return compraRepository.save(compra);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la Compra", e);
        }
    }
@Transactional
    public void actualizarDevolucionYDescripcion(Integer id, Boolean devuelto, String descripcion) {
        int updatedRows = compraRepository.actualizarDevolucionYDescripcion(id, devuelto, descripcion);
        if (updatedRows == 0) {
            throw new RuntimeException("No se pudo actualizar la compra con ID: " + id);
        }
    }
}