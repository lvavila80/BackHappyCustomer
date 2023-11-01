package com.project.Project.project.service;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.ArticuloRepository;
import com.project.Project.project.repository.DetalleVentaRepository;
import com.project.Project.project.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Transactional
    public void createVenta(VentaArticuloDTO ventaArticulosDTO) {
        try {
            Venta venta = new Venta();
            Double valorTotal = 0.00;

            if (!isValidVentaData(ventaArticulosDTO)) {
                throw new RuntimeException("Los datos de la venta son incorrectos.");
            }

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
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_usuario: " + e.getMessage(), e);
            }

            try {
                ventaRepository.insertVentaCliente(savedVenta.getId(), ventaArticulosDTO.getIdCliente());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en venta_cliente: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar venta y relaciones: " + e.getMessage(), e);
        }
    }

    public boolean isValidVentaData(VentaArticuloDTO ventaArticuloDTO) {
        List<ArticuloVentaDTO> articulosVenta = ventaArticuloDTO.getArticulosVenta();
        if (articulosVenta == null || articulosVenta.isEmpty()) {
            return false;
        }

        for (ArticuloVentaDTO articuloVenta : articulosVenta) {
            if (articuloVenta.getArticulo() <= 0 || articuloVenta.getUnidadesVendidas() <= 0) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public void revertirVenta(Long idVenta, String detalleDevolucion, ArrayList array) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByIdventa(idVenta);
        Boolean encontrado = false;
        for (DetalleVenta detalle : detalles) {
            try {
                if( array.contains(detalle.getIdarticulo()) && detalle.getEstado() != null && detalle.getEstado().equals("devuelto") ){
                    throw new RuntimeException("El articulo " + detalle.getIdarticulo() + " ya se encuentra devuelto");
                }else if(array.contains(detalle.getIdarticulo()) && (detalle.getEstado() == null || !detalle.getEstado().equals("devuelto"))) {
                    detalle.setEstado("devuelto");
                    detalle.setDetalleDevolucion(detalleDevolucion);
                    detalleVentaRepository.save(detalle);
                    Articulo articulo = articuloRepository.findById(detalle.getIdarticulo()).get();
                    int nuevasUnidades = ((articulo.getUnidadesdisponibles())+(detalle.getUnidadesvendidas()));
                    articuloRepository.updateUnidadesDisponiblesById(detalle.getIdarticulo(), nuevasUnidades);
                    encontrado = true;
                }

            } catch (Exception e) {
                throw new RuntimeException("Error al reversar venta. " +e.getMessage());
            }
        }
        if(!encontrado){
            throw new RuntimeException("Error, el id del articulo no corresponde a los articulos de esta venta. " );
        }
    }
}