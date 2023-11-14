package com.project.Project.project.service;
import com.project.Project.project.model.*;
import com.project.Project.project.model.ArticulosCompraDTO;
import com.project.Project.project.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ArticuloService articuloService;
    @Autowired
    private DetalleCompraService detalleCompraService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private boolean devolucionEsPermitida(Integer idCompra) {
        Optional<Compra> compraOpt = compraRepository.findById(idCompra);
        if (!compraOpt.isPresent()) {
            throw new RuntimeException("Error, No existe una compra con este id.");
        }
        Compra compra = compraOpt.get();
        LocalDate fechaCompra = compra.getFechaCompra(); // Reemplaza 'getFechaCompra' con el método getter real de tu entidad Compra
        LocalDate fechaActual = LocalDate.now(ZoneId.systemDefault());
        return ChronoUnit.MONTHS.between(fechaCompra, fechaActual) <= 3;
    }
    @Transactional

    public void guardarCompraYRelaciones(CompraArticulosDTO compraArticulosDTO) {


        Optional<Proveedor> proveedor = proveedorRepository.findById((long) compraArticulosDTO.getIdProveedor());
        if(proveedor.isEmpty()){
            throw new RuntimeException("El cliente no se encuentra registrado en el sistema.");
        }

        Optional<Usuario> usuario = usuarioRepository.findById((compraArticulosDTO.getIdUsuario()));
        if(usuario.isEmpty()){
            throw new RuntimeException("El usuario no se encuentra autorizado para realizar esta operación, o no existe en el sistema.");
        }
        Double valorTotal = 0.00;
        List<Categoria> categorias;
        categorias = categoriaRepository.findAll();
        for(ArticulosCompraDTO articuloCompra : compraArticulosDTO.getArticulosCompra()) {
            if (categorias.stream().noneMatch(categoria -> categoria.getId() == articuloCompra.getIdCategoria())) {
                throw new RuntimeException("Error, no existe la categoría con el id :"+articuloCompra.getIdCategoria());
            }
            valorTotal += (articuloCompra.getValorUnidad()*articuloCompra.getUnidadesCompradas());
        }
        Compra compra = new Compra(valorTotal);
        Compra savedCompra = compraRepository.save(compra);
        if (savedCompra == null || savedCompra.getId() == null) {
            throw new RuntimeException("Error al guardar la Compra");
        }

        for(ArticulosCompraDTO articuloCompra : compraArticulosDTO.getArticulosCompra()) {
            try {
                Integer idArticulo = articuloService.guardarArticulo(articuloCompra);
                DetalleCompra detalleCompra = detalleCompraService.guardarDetalleCompra(articuloCompra,savedCompra,idArticulo);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar compra y relaciones: " + e.getMessage(), e);
            }
        }

        try {
            compraRepository.insertCompraProveedor(compraArticulosDTO.getIdProveedor(), savedCompra.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar en bd.compra_proveedor: " + e.getMessage());
        }

        try {
            compraRepository.insertCompraUsuario(savedCompra.getId(), compraArticulosDTO.getIdUsuario());
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar en bd.compra_usuario: " + e.getMessage());
        }

    }

    @Transactional

    public void actualizarDevolucion(Integer idCompra, String detalleDevolucion, ArrayList array) {
        List<DetalleCompra> detalles = detalleCompraRepository.findByIdcompra(idCompra);
        Compra compra = compraRepository.findById(idCompra)
                .orElseThrow(() -> new RuntimeException("Error, No existe una compra con este id."));

        // Verificar si han pasado más de tres meses desde la fecha de la compra
        Date fechaActual = new Date();
        long diferenciaEnMiliseg = fechaActual.getTime() - compra.getFechacompra().getTime();
        long diasDiferencia = TimeUnit.MILLISECONDS.toDays(diferenciaEnMiliseg);

        if (diasDiferencia > (30 * 3)) { // Asumiendo aproximadamente 30 días por mes
            throw new RuntimeException("No se puede realizar la devolución, han pasado más de tres meses desde la compra.");
        }
        if(detalles.isEmpty()){
            throw new RuntimeException("Error, No existe una compra con este id. " );
        }
        Boolean encontrado = false;
        for (DetalleCompra detalle : detalles) {
            try {
                if( array.contains(detalle.getIdarticulo()) && detalle.getEstado() != null && detalle.getEstado().equals("devuelto") ){
                    throw new RuntimeException("El articulo " + detalle.getIdarticulo() + " ya se encuentra devuelto.");
                }else if(array.contains(detalle.getIdarticulo()) && (detalle.getEstado() == null || !detalle.getEstado().equals("devuelto"))) {
                    detalle.setEstado("devuelto");
                    detalle.setDetalleDevolucion(detalleDevolucion);
                    detalleCompraRepository.save(detalle);
                    Articulo articulo = articuloRepository.findById(detalle.getIdarticulo()).get();
                    int nuevasUnidades = ((articulo.getUnidadesdisponibles())-(detalle.getUnidadescompradas()));
                    articuloRepository.updateUnidadesDisponiblesById(detalle.getIdarticulo(), nuevasUnidades);
                    encontrado = true;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al reversar venta. " +e.getMessage());
            }
        }
        if(!encontrado){
            throw new RuntimeException("Error, el id del articulo no corresponde a los articulos de esta compra. " );
        }
    }


}