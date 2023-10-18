package com.project.Project.project.service;

import com.project.Project.project.model.Venta;
import com.project.Project.project.repository.VentaRepository;
import com.project.Project.project.model.ArticulosVentaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Transactional
    public Venta createVenta(Venta venta) {
        try {

            Venta savedVenta = ventaRepository.save(venta);

            try {
                ventaRepository.insertVentaUsuario(savedVenta.getId(), venta.getIdUsuario());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_usuario: " + e.getMessage(), e);
            }

            try {
                ventaRepository.insertVentaCliente(savedVenta.getId(), venta.getIdCliente());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_cliente: " + e.getMessage(), e);
            }

            try {
                ventaRepository.insertVentaCategoria(savedVenta.getId(), venta.getIdCategoria());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_categoria: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta y relaciones: " + e.getMessage(), e);
        }

        return ventaRepository.save(venta);
    }
}

