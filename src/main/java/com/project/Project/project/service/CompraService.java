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
            Integer idArticulo = articuloService.guardarArticulo(compraArticuloDTO);
            Compra savedCompra = guardarCompra(compraArticuloDTO.getCompra());

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
                compraRepository.insertCompraCategoria(idCompra, compraArticuloDTO.getIdCategoria());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.compra_categoria: " + e.getMessage());
            }

            try {
                compraRepository.insertCompraProveedor(compraArticuloDTO.getIdProveedor(), idCompra);
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.compra_proveedor: " + e.getMessage());
            }

            try {
                compraRepository.insertCompraUsuario(idCompra, compraArticuloDTO.getIdUsuario());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.compra_usuario: " + e.getMessage());
            }

            try {
                compraRepository.insertArticuloCategoria(idArticulo, compraArticuloDTO.getIdCategoria());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.articulo_categoria: " + e.getMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar compra y relaciones: " + e.getMessage(), e);
        }
    }
    public Compra guardarCompra(Compra compra) {
        try {
            if(compra.getValortotal()==0){
                compra.setValortotal(compra.getValorunidad()*compra.getUnidadescompradas());
            }
            return compraRepository.save(compra);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la Compra", e);
        }
    }
}