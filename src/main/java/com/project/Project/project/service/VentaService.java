package com.project.Project.project.service;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void createVenta(VentaArticuloDTO ventaArticulosDTO) {
        try {

            Optional<Cliente> cliente = clienteRepository.findById((long) ventaArticulosDTO.getIdCliente());
            if(cliente.isEmpty()){
                throw new RuntimeException("El cliente no se encuentra registrado en el sistema.");
            }

            Optional<Usuario> usuario = usuarioRepository.findById((ventaArticulosDTO.getIdUsuario()));
            if(usuario.isEmpty()){
                throw new RuntimeException("El usuario no se encuentra autorizado para realizar esta operación, o no existe en el sistema.");
            }

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
    public void revertirVenta(Long idVenta, String detalleDevolucion, ArrayList<Integer> articulosDevueltos) {
        List<DetalleVenta> detallesVenta = detalleVentaRepository.findByIdventa((long) idVenta.intValue());
        if (detallesVenta.isEmpty()) {
            throw new RuntimeException("Error, No existe una Venta con este id: " + idVenta);
        }
        for (DetalleVenta detalle : detallesVenta) {

            if (articulosDevueltos.contains(detalle.getIdarticulo())) {
                if ("devuelto".equals(detalle.getEstado())) {
                    continue;
                }
                detalle.setEstado("art devuelto");
                detalle.setDetalleDevolucion(detalleDevolucion);
                detalleVentaRepository.save(detalle);

                Articulo articulo = articuloRepository.findById(detalle.getIdarticulo())
                        .orElseThrow(() -> new RuntimeException("Artículo no encontrado con ID: " + detalle.getIdarticulo()));

                int nuevasUnidades = articulo.getUnidadesdisponibles() + detalle.getUnidadesvendidas();
                articulo.setUnidadesdisponibles(nuevasUnidades);
                articuloRepository.save(articulo);
            }
        }
    }
}