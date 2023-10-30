package com.project.Project.project.service;

import com.project.Project.project.model.ArticulosCompraDTO;
import com.project.Project.project.model.Venta;
import com.project.Project.project.repository.VentaRepository;
import com.project.Project.project.model.ArticuloVentaDTO;
import com.project.Project.project.model.VentaArticuloDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Transactional
    public void createVenta(VentaArticuloDTO ventaArticulosDTO) {
        try {

            Venta venta=new Venta();
            Double valorTotal = 0.00;

            for(ArticuloVentaDTO articuloVenta : ventaArticulosDTO.getArticulosVenta()) {
                valorTotal += (articuloVenta.getValorUnidad()*articuloVenta.getUnidadesVendidas());
            }
            venta.setValorTotal(valorTotal);
            Venta savedVenta = ventaRepository.save(venta);

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
                ventaRepository.insertVentaCategoria(savedVenta.getId(), ventaArticulosDTO.getIdCategoria());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_categoria: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta y relaciones: " + e.getMessage(), e);
        }

    }
}

