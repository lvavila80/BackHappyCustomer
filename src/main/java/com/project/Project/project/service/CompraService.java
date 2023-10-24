package com.project.Project.project.service;
import com.project.Project.project.model.*;
import com.project.Project.project.model.ArticulosCompraDTO;
import com.project.Project.project.repository.CategoriaRepository;
import com.project.Project.project.repository.CompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ArticuloService articuloService;
    @Autowired
    private DetalleCompraService detalleCompraService;

    @Autowired
    private CategoriaRepository categoriaRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void guardarCompraYRelaciones(CompraArticulosDTO compraArticulosDTO) {
        Double valorTotal = 0.00;
        List<Categoria> categorias;
        categorias = categoriaRepository.findAll();
        for(ArticulosCompraDTO articuloCompra : compraArticulosDTO.getArticulosCompra()) {
            if (categorias.stream().noneMatch(categoria -> categoria.getId() == articuloCompra.getIdCategoria())) {
                throw new RuntimeException("Error, no existe la categoría con el id :"+articuloCompra.getIdCategoria());
            }
            valorTotal += (articuloCompra.getValorUnidad()*articuloCompra.getUnidadesCompradas());
        }
        Compra compra = new Compra(valorTotal);
        Compra savedCompra = compraRepository.save(compra);
        if (savedCompra == null || savedCompra.getId() == null) {
            throw new RuntimeException("Error al guardar la Compra");
        }

        for(ArticulosCompraDTO articuloCompra : compraArticulosDTO.getArticulosCompra()) {
            try {
                Integer idArticulo = articuloService.guardarArticulo(articuloCompra);
                DetalleCompra detalleCompra = detalleCompraService.guardarDetalleCompra(articuloCompra,savedCompra,idArticulo);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar compra y relaciones: " + e.getMessage(), e);
            }
        }

        try {
            compraRepository.insertCompraProveedor(compraArticulosDTO.getIdProveedor(), savedCompra.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar en bd.compra_proveedor: " + e.getMessage());
        }

        try {
            compraRepository.insertCompraUsuario(savedCompra.getId(), compraArticulosDTO.getIdUsuario());
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar en bd.compra_usuario: " + e.getMessage());
        }

    }

    @Transactional
    public void actualizarDevolucion(Integer idCompra, String descripcion, Boolean devuelto) {
        compraRepository.updateDevolucionInfo(idCompra, descripcion, devuelto);
        detalleCompraService.reversarCompra(idCompra);
    }

}