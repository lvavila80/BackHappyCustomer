package com.project.Project.project.service;

import com.project.Project.project.model.ArticuloVentaDTO;
import com.project.Project.project.model.Venta;
import com.project.Project.project.model.DetalleVenta;
import com.project.Project.project.repository.DetalleVentaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ArticuloService articuloService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public DetalleVenta guardarDetalleVenta(ArticuloVentaDTO ventaArticulosDTO, Venta savedVenta, int idArticulo) {
        try {
            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setIdVenta(idArticulo);
            detalleVenta.setIdCategoria(ventaArticulosDTO.getIdCategoria());
            detalleVenta.setUnidadesVendidas(idArticulo);
            detalleVenta.setIdArticulo(idArticulo);
            return detalleVentaRepository.save(detalleVenta);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta y relaciones: " + e.getMessage(), e);
        }
    }
}
