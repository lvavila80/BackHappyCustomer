package com.project.Project.project.service;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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

    public void revertirVenta(int idVenta, String detalleDevolucion, List<ProductoRevertidoDTO> productosDevueltos, boolean confirmacionUsuario) {
        if (!confirmacionUsuario) {
            throw new RuntimeException("La confirmación del usuario es requerida para proceder con la reversión.");
        }

        long idVentaLong = (long) idVenta;
        Optional<Venta> optionalVenta = ventaRepository.findById(idVentaLong);
        if (!optionalVenta.isPresent()) {
            throw new RuntimeException("Error, No existe una Venta con este id.");
        }

        Venta venta = optionalVenta.get();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaEnBD = dateFormat.format(venta.getFechaVenta());
        LocalDate fechaBD = LocalDate.parse(fechaEnBD, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate fechaHoy = LocalDate.now();
        Period periodo = Period.between(fechaBD, fechaHoy);
        int diferenciaMeses = periodo.getMonths();
        if (diferenciaMeses > 3) {
            throw new RuntimeException("Error, La venta fue realizada hace más de 3 meses.");
        }

        List<DetalleVenta> detalles = detalleVentaRepository.findByIdventa(idVenta);
        if (detalles.isEmpty()) {
            throw new RuntimeException("Error, No existe una Venta con este id.");
        }

        for (ProductoRevertidoDTO producto : productosDevueltos) {
            boolean encontrado = false;
            for (DetalleVenta detalle : detalles) {
                if (detalle.getIdarticulo() == producto.getIdArticulo()) {
                    if (detalle.getEstado() != null && detalle.getEstado() == 4) {
                        throw new RuntimeException("El articulo " + detalle.getIdarticulo() + " ya se encuentra devuelto");
                    }

                    if (producto.getCantidad() > detalle.getUnidadesvendidas()) {
                        throw new RuntimeException("Cantidad a devolver del articulo " + detalle.getIdarticulo() + " es mayor a la vendida.");
                    }

                    detalle.setEstado(4);
                    detalle.setDetalleDevolucion(detalleDevolucion);
                    detalleVentaRepository.save(detalle);

                    Articulo articulo = articuloRepository.findById(detalle.getIdarticulo())
                            .orElseThrow(() -> new RuntimeException("Artículo no encontrado con ID: " + detalle.getIdarticulo()));

                    int nuevasUnidades = articulo.getUnidadesdisponibles() + producto.getCantidad();
                    articuloRepository.updateUnidadesDisponiblesById(detalle.getIdarticulo(), nuevasUnidades);
                    encontrado = true;
                    break; // Rompe el bucle interno si encuentra y procesa el artículo
                }
            }

            if (!encontrado) {
                throw new RuntimeException("Error, el id del artículo " + producto.getIdArticulo() + " no corresponde a los artículos de esta venta.");
            }
        }
    }

    public void actualizarEstadoVenta(int idVenta, articulosEstadoDTO nuevoEstado){
        if(nuevoEstado.getEstado()==4){
            throw new RuntimeException("Error, No puede hacer devoluciones a través de este modulo, use el modulo correcto.");
        }
        List<DetalleVenta> detalleVenta= detalleVentaService.getDetallesVentaByIdcompra(idVenta);
        if(!detalleVenta.isEmpty()){
            for (DetalleVenta detalle : detalleVenta){
                if(detalle.getEstado() == nuevoEstado.getEstado()){
                    throw new RuntimeException("Error, esta venta ya tiene este estado.");
                }
                if(detalle.getIdarticulo() == nuevoEstado.getId()){
                    if (detalle.getEstado()== 4 || detalle.getEstado()==3) {
                        throw new RuntimeException("Error, la venta ya no puede cambiar de estado.");
                    }
                    if(detalle.getEstado()==2 && !(nuevoEstado.getEstado()==4)){
                        throw new RuntimeException("Error, las ventas confirmadas solo pueden devolverse.");
                    }
                    if(detalle.getEstado()==1 && nuevoEstado.getEstado()==4){
                        throw new RuntimeException("Error, la venta no puede pasar de Pendiente a Devuelto.");
                    }
                    if (nuevoEstado.getEstado()==3 && !(detalle.getEstado()==1)){
                        throw new RuntimeException("Error, la venta solo puede cancelarse si su estado es Pendiente.");
                    }
                    if(nuevoEstado.getEstado()>4){
                        throw new RuntimeException("Error, estado invalido para este proceso.");
                    }
                }
            }
            for (DetalleVenta detalle : detalleVenta){
                if(detalle.getIdarticulo() == nuevoEstado.getId()) {
                    detalle.setEstado(nuevoEstado.getEstado());
                    detalleVentaRepository.save(detalle);
                }
            }
        }else{
            throw new RuntimeException("Error, compra inexistente." );
        }
    }

}