package com.project.Project.project.service;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.*;

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
    @Autowired
    private EmailService emailService;

    private Map<Long, Integer> intentosReversion = new HashMap<>(); // Mapa para rastrear intentos de reversión

    @Transactional
    public void createVenta(VentaArticuloDTO ventaArticulosDTO) {
        try {

            Optional<Cliente> cliente = clienteRepository.findById((long) ventaArticulosDTO.getIdCliente());
            if (cliente.isEmpty()) {
                throw new RuntimeException("El cliente no se encuentra registrado en el sistema.");
            }

            Optional<Usuario> usuario = usuarioRepository.findById((ventaArticulosDTO.getIdUsuario()));
            if (usuario.isEmpty()) {
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

    public boolean revertirVenta(ReversionVentaDTO reversionVentaDTO, String emailDestinatario) {
        long idVentaLong = (long) reversionVentaDTO.getIdVenta();
        intentosReversion.putIfAbsent(idVentaLong, 0);
        int intentos = intentosReversion.get(idVentaLong) + 1;
        intentosReversion.put(idVentaLong, intentos);

        Optional<Venta> optionalVenta = ventaRepository.findById(idVentaLong);
        if (!optionalVenta.isPresent()) {
            throw new RuntimeException("Error, No existe una Venta con este id.");
        }

        Venta venta = optionalVenta.get();
        List<DetalleVenta> detalles = detalleVentaRepository.findByIdventa(reversionVentaDTO.getIdVenta());
        if (detalles.isEmpty()) {
            throw new RuntimeException("Error, No existe una Venta con este id.");
        }

        boolean esVentaConfirmada = detalles.stream().allMatch(detalle -> detalle.getEstado() == 2);
        if (!esVentaConfirmada) {
            if (intentos >= 2) {
                enviarCorreoError(emailDestinatario, idVentaLong, "Error, solo se pueden revertir ventas en estado confirmado.");
            }
            throw new RuntimeException("Error, solo se pueden revertir ventas en estado confirmado.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaEnBD = dateFormat.format(venta.getFechaVenta());
        LocalDate fechaBD = LocalDate.parse(fechaEnBD, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate fechaHoy = LocalDate.now();
        Period periodo = Period.between(fechaBD, fechaHoy);
        int diferenciaMeses = periodo.getMonths();
        if (diferenciaMeses > 3) {
            throw new RuntimeException("Error, La venta fue realizada hace más de 3 meses.");
        }

        for (Integer producto : reversionVentaDTO.getDevuelto()) {
            boolean encontrado = false;
            for (DetalleVenta detalle : detalles) {
                if (detalle.getIdarticulo() == producto) {
                    if (detalle.getEstado() != null && detalle.getEstado() == 4) {
                        throw new RuntimeException("El articulo " + detalle.getIdarticulo() + " ya se encuentra devuelto");
                    }

                    detalle.setEstado(4);
                    detalle.setDetalleDevolucion(reversionVentaDTO.getMotivoReversion());
                    detalleVentaRepository.save(detalle);

                    Articulo articulo = articuloRepository.findById(detalle.getIdarticulo())
                            .orElseThrow(() -> new RuntimeException("Artículo no encontrado con ID: " + detalle.getIdarticulo()));
                    int nuevasUnidades = articulo.getUnidadesdisponibles() + detalle.getUnidadesvendidas();
                    articulo.setUnidadesdisponibles(nuevasUnidades);
                    articuloRepository.save(articulo);

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                throw new RuntimeException("Error, el id del artículo " + producto + " no corresponde a los artículos de esta venta.");
            }
        }

        return true;
    }

    private void enviarCorreoError(String emailDestinatario, long idVenta, String mensajeError) {
        try {
            String asunto = "LAUREN- Error en la Reversión de Venta, segunda solicitud (2), venta en estado cancelado porque ya se reversó, envío de error - Venta ID: " + idVenta;
            emailService.sendSimpleMessage(emailDestinatario, asunto, mensajeError);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo electrónico: " + e.getMessage());
        }
    }

    public void actualizarEstadoVenta(int idVenta, articulosEstadoDTO nuevoEstado) {
        if (nuevoEstado.getEstado() == 4) {
            throw new RuntimeException("Error, No puede hacer devoluciones a través de este modulo, use el modulo correcto.");
        }

        List<DetalleVenta> detalleVenta = detalleVentaService.getDetallesVentaByIdcompra(idVenta);
        if (!detalleVenta.isEmpty()) {
            for (DetalleVenta detalle : detalleVenta) {
                if (detalle.getEstado() == nuevoEstado.getEstado()) {
                    throw new RuntimeException("Error, esta venta ya tiene este estado.");
                }
                if (detalle.getIdarticulo() == nuevoEstado.getId()) {
                    if (detalle.getEstado() == 4 || detalle.getEstado() == 3) {
                        throw new RuntimeException("Error, la venta ya no puede cambiar de estado.");
                    }
                    if (detalle.getEstado() == 2 && !(nuevoEstado.getEstado() == 4)) {
                        throw new RuntimeException("Error, las ventas confirmadas solo pueden devolverse.");
                    }
                    if (detalle.getEstado() == 1 && nuevoEstado.getEstado() == 4) {
                        throw new RuntimeException("Error, la venta no puede pasar de Pendiente a Devuelto.");
                    }
                    if (nuevoEstado.getEstado() == 3 && !(detalle.getEstado() == 1)) {
                        throw new RuntimeException("Error, la venta solo puede cancelarse si su estado es Pendiente.");
                    }
                    if (nuevoEstado.getEstado() > 4) {
                        throw new RuntimeException("Error, estado invalido para este proceso.");
                    }
                }
            }

            for (DetalleVenta detalle : detalleVenta) {
                if (detalle.getIdarticulo() == nuevoEstado.getId()) {
                    detalle.setEstado(nuevoEstado.getEstado());
                    detalleVentaRepository.save(detalle);
                }
            }
        } else {
            throw new RuntimeException("Error, compra inexistente.");
        }
    }


}
