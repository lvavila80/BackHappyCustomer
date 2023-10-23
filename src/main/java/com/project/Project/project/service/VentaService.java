package com.project.Project.project.service;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Date;

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

                if (encontrado != null && encontrado.getUnidadesdisponibles() >= unidadesVendidas) {
                    articuloService.findByIdAndUpdateUnidadesDisponibles(encontrado.getId(),
                            (encontrado.getUnidadesdisponibles() - unidadesVendidas));

                    encontrado.getValorunitario();
                    valorTotal += (encontrado.getValorunitario() * unidadesVendidas);
                } else {
                    throw new RuntimeException("No hay suficiente stock para el artículo con ID: " + idArticulo);
                }
            }

            venta.setFechaVenta(new Date());
            venta.setValorTotal(valorTotal);

            Venta savedVenta = ventaRepository.save(venta);

            for (ArticuloVentaDTO articuloVenta : ventaArticulosDTO.getArticulosVenta()) {
                int idArticulo = articuloVenta.getArticulo();
                Articulo encontrado = articuloService.findById(idArticulo);

                if (encontrado != null) {
                    DetalleVenta detalleVenta = detalleVentaService.guardarDetalleVenta(articuloVenta, savedVenta, encontrado.getId());
                }
            }

            try {
                ventaRepository.insertVentaUsuario(savedVenta.getId(), ventaArticulosDTO.getIdUsuario());
                ventaRepository.insertVentaCliente(savedVenta.getId(), ventaArticulosDTO.getIdCliente());

            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en la base de datos: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta y relaciones: " + e.getMessage(), e);
        }
    }

}