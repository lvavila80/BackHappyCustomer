package com.project.Project.project.service;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Transactional
    public void createVenta(VentaArticuloDTO ventaArticulosDTO) {
        try {
            Venta venta = new Venta();
            Double valorTotal = 0.00;

            for (ArticuloVentaDTO articuloVenta : ventaArticulosDTO.getArticulosVenta()) {
                int idArticulo = articuloVenta.getArticulo();
                int unidadesVendidas = articuloVenta.getUnidadesVendidas();

                Articulo encontrado = articuloService.findById(idArticulo);

                if (encontrado.getId() != null) {
                    articuloService.findByIdAndUpdateUnidadesDisponibles(encontrado.getId(),
                            (encontrado.getUnidadesdisponibles() - unidadesVendidas));
                }

                encontrado.getValorunitario();
                valorTotal += (encontrado.getValorunitario() * unidadesVendidas);
            }

            venta.setValorTotal(valorTotal);
            Venta savedVenta = ventaRepository.save(venta);

            ArticuloVentaDTO articuloVenta = null;  // Variable para almacenar articuloVenta

            for (ArticuloVentaDTO av : ventaArticulosDTO.getArticulosVenta()) {
                articuloVenta = av;  // Asignar valor a la variable
                try {
                    int idArticulo = articuloVenta.getArticulo();
                    Articulo encontrado = articuloService.findById(idArticulo);

                    if (encontrado.getId() != null) {
                        articuloService.findByIdAndUpdateUnidadesDisponibles(encontrado.getId(),
                                (encontrado.getUnidadesdisponibles() - articuloVenta.getUnidadesVendidas()));
                    }

                    DetalleVenta detalleVenta = detalleVentaService.guardarDetalleVenta(articuloVenta,
                            savedVenta, encontrado.getId());
                } catch (Exception e) {
                    throw new RuntimeException("Error al guardar compra y relaciones: " + e.getMessage(), e);
                }
            }

            try {
                ventaRepository.insertVentaUsuario(savedVenta.getId(), ventaArticulosDTO.getIdUsuario());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_usuario: " + e.getMessage(), e);
            }

            try {
                ventaRepository.insertVentaCliente(savedVenta.getId(), ventaArticulosDTO.getIdCliente());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_cliente: " + e.getMessage(), e);
            }

            try {
                ventaRepository.insertVentaCategoria(savedVenta.getId(), articuloVenta.getIdCategoria());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_categoria: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta y relaciones: " + e.getMessage(), e);
        }
    }
}
