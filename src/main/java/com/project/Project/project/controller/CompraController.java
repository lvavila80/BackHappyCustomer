package com.project.Project.project.controller;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.ArticuloRepository;
import com.project.Project.project.repository.CompraRepository;
import com.project.Project.project.repository.DetalleCompraRepository;
import com.project.Project.project.service.CompraService;
import com.project.Project.project.service.ErrorLoggingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private ErrorLoggingService errorLoggingService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private CompraRepository compraRepository;

    @PostMapping("/registrarCompra")
    public ResponseEntity<Map<String, String>> agregarCompra(@Valid @RequestBody CompraArticulosDTO compraArticulosDTO) {
        try {
            // Agregar logging para verificar el contenido del DTO recibido
            System.out.println("Recibido DTO: " + compraArticulosDTO);

            compraService.guardarCompraYRelaciones(compraArticulosDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Compra y artículo agregados exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            errorLoggingService.logError("Error en CompraController - registrarCompra", e, compraArticulosDTO.toString());
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/devolucionCompra")
    public ResponseEntity<String> actualizarDevolucion(@Valid @RequestBody DevoUpdateDTO devoUpdateDTO) {
        try {
            compraService.actualizarDevolucion(devoUpdateDTO.getIdCompra(), devoUpdateDTO.getDescripcion(), devoUpdateDTO.getDevuelto());
            return new ResponseEntity<>("Devolución exitosa", HttpStatus.OK);
        } catch (RuntimeException e) {
            errorLoggingService.logError("Error en CompraController - devolucionCompra", e, devoUpdateDTO.toString());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/estadoCompra")
    public ResponseEntity<String> actualizarEstadoCompra(@RequestBody EstadosDTO estadosDTO) {
        try {
            int idCompra = estadosDTO.getOperacion();
            for (articulosEstadoDTO estado : estadosDTO.getArticulos()) {
                compraService.actualizarEstadoCompra(idCompra, estado);
            }
            return new ResponseEntity<>("Se cambió el estado", HttpStatus.OK);
        } catch (Exception e) {
            errorLoggingService.logError("Error en CompraController - actualizarEstadoCompra", e, "");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/detalleCompra")
    public ResponseEntity<List<com.project.Project.project.dto.DetalleCompraDTO>> obtenerDetalleCompra() {
        List<DetalleCompra> detallesCompra = detalleCompraRepository.findAll();
        List<com.project.Project.project.dto.DetalleCompraDTO> detallesCompraDTO = new ArrayList<>();

        for (DetalleCompra detalle : detallesCompra) {
            com.project.Project.project.dto.DetalleCompraDTO detalleDTO = new com.project.Project.project.dto.DetalleCompraDTO();
            detalleDTO.setId(detalle.getId());
            detalleDTO.setIdcompra(detalle.getIdcompra());
            detalleDTO.setIdarticulo(detalle.getIdarticulo());
            detalleDTO.setUnidadescompradas(detalle.getUnidadescompradas());
            detalleDTO.setValorunidad(detalle.getValorunidad());
            detalleDTO.setEstado(detalle.getEstado());
            detalleDTO.setDetalleDevolucion(detalle.getDetalleDevolucion());

            // Obtener el nombre del artículo usando su idArticulo
            Optional<Articulo> articuloOptional = articuloRepository.findById(detalle.getIdarticulo());
            String nombreArticulo = articuloOptional.map(Articulo::getNombrearticulo).orElse("Nombre no encontrado");
            detalleDTO.setNombreArticulo(nombreArticulo);

            // Obtener la compra relacionada
            Optional<Compra> compraOptional = compraRepository.findById(detalle.getIdcompra());
            if (compraOptional.isPresent()) {
                Compra compra = compraOptional.get();
                detalleDTO.setValortotal(compra.getValortotal());
                detalleDTO.setFechacompra(compra.getFechacompra());
            }

            detallesCompraDTO.add(detalleDTO);
        }

        return ResponseEntity.ok(detallesCompraDTO);
    }

}
