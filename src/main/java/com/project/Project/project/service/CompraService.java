package com.project.Project.project.service;
import com.project.Project.project.model.Compra;
import com.project.Project.project.model.CompraArticuloDTO;
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
    public void guardarCompraYRelaciones(CompraArticuloDTO compraArticuloDTO) {
        try {
            Integer idArticulo = articuloService.guardarArticulo(compraArticuloDTO.getArticulo());
            Compra savedCompra = guardarCompra(compraArticuloDTO.getCompra());

            if(savedCompra == null || savedCompra.getId() == null) {
                throw new RuntimeException("Error al guardar la Compra");
            }

            Integer idCompra = savedCompra.getId();
            try {
                entityManager.createNativeQuery("INSERT INTO bd.compra_articulo (idarticulo, idcompra) VALUES (?, ?)")
                        .setParameter(1, idArticulo)
                        .setParameter(2, idCompra)
                        .executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.compra_articulo: " + e.getMessage());
            }

            try {
                entityManager.createNativeQuery("INSERT INTO bd.compra_categoria (idcompra, idcategoria) VALUES (?, ?)")
                        .setParameter(1, idCompra)
                        .setParameter(2, compraArticuloDTO.getIdCategoria())
                        .executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.compra_categoria: " + e.getMessage());
            }

            try {
                entityManager.createNativeQuery("INSERT INTO bd.compra_proveedor (idproveedor, idcompra) VALUES (?, ?)")
                        .setParameter(1, compraArticuloDTO.getIdProveedor())
                        .setParameter(2, idCompra)
                        .executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.compra_proveedor: " + e.getMessage());
            }

            try {
                entityManager.createNativeQuery("INSERT INTO bd.compra_usuario (idcompra, idusuario) VALUES (?, ?)")
                        .setParameter(1, idCompra)
                        .setParameter(2, compraArticuloDTO.getIdUsuario())
                        .executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.compra_usuario: " + e.getMessage());
            }

            try {
                entityManager.createNativeQuery("INSERT INTO bd.articulo_categoria (idarticulo, idcategoria) VALUES (?, ?)")
                        .setParameter(1, idArticulo)
                        .setParameter(2, compraArticuloDTO.getIdCategoria())
                        .executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.articulo_categoria: " + e.getMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar compra y relaciones: " + e.getMessage(), e);
        }
    }
    public Compra guardarCompra(Compra compra) {
        try {
            return compraRepository.save(compra);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la Compra", e);
        }
    }
}