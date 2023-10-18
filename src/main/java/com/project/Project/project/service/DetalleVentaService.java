package com.project.Project.project.service;

import com.project.Project.project.model.Articulo;
import com.project.Project.project.model.ArticuloVentaDTO;
import com.project.Project.project.model.Venta;
import com.project.Project.project.model.DetalleVenta;
import com.project.Project.project.repository.DetalleVentaRepository;
import com.project.Project.project.repository.VentaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private VentaRepository ventaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public DetalleVenta guardarDetalleVenta(ArticuloVentaDTO ventaArticulosDTO, Venta savedVenta, int idArticulo) {
        try {
            Long ventaId = (long) savedVenta.getId();

            Optional<Venta> ventaOptional = ventaRepository.findById(ventaId);
            if (ventaOptional.isEmpty()) {
                throw new RuntimeException("La venta con el id " + ventaId + " no existe.");
            }

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setIdventa(ventaId.intValue());
            detalleVenta.setIdcategoria(ventaArticulosDTO.getIdCategoria());
            detalleVenta.setUnidadesvendidas(idArticulo);
            detalleVenta.setIdarticulo(idArticulo);

            Articulo articulo = articuloService.findById(idArticulo);
            if (articulo != null) {
                detalleVenta.setValorunidad(articulo.getValorunitario());
            }

            return detalleVentaRepository.save(detalleVenta);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta y relaciones: " + e.getMessage(), e);
        }
    }
}


