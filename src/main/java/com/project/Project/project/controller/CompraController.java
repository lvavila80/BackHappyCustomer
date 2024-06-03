package com.project.Project.project.controller;

import com.project.Project.project.model.CompraArticulosDTO;
import com.project.Project.project.model.DevoUpdateDTO;
import com.project.Project.project.model.EstadosDTO;
import com.project.Project.project.model.articulosEstadoDTO;
import com.project.Project.project.service.CompraService;
import com.project.Project.project.service.ErrorLoggingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private ErrorLoggingService errorLoggingService;

    @Autowired
    private CompraService compraService;

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
            for(articulosEstadoDTO estado : estadosDTO.getArticulos()){
                compraService.actualizarEstadoCompra(idCompra,estado);
            }
            return new ResponseEntity<>("Se cambió el estado", HttpStatus.OK);
        } catch (Exception e) {
            errorLoggingService.logError("Error en CompraController - actualizarEstadoCompra", e, "");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
