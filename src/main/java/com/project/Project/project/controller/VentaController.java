package com.project.Project.project.controller;

import com.project.Project.project.model.*;
import com.project.Project.project.service.ErrorLoggingService;
import com.project.Project.project.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ErrorLoggingService errorLoggingService;

    @PostMapping("/nuevaVenta")
    public ResponseEntity<Object> createVenta(@Valid @RequestBody VentaArticuloDTO ventaArticuloDTO) {
        try {
            ventaService.createVenta(ventaArticuloDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Venta registrada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            errorLoggingService.logError("Error en VentaController - createVenta", e, ventaArticuloDTO.toString());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear la venta: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/devolucionVenta")
    public ResponseEntity<Object> revertirVenta(@Valid @RequestBody ReversionVentaDTO reversionVentaDTO) {
        try {
            String emailDestinatario = "raranda@ucatolica.edu.co";

            boolean exito = ventaService.revertirVenta(reversionVentaDTO, emailDestinatario);
            if (exito) {
                Map<String, String> response = new HashMap<>();
                response.put("message", String.format("Artículos devueltos exitosamente. ID de la Venta: %d, Fecha y hora de la reversión: %s",
                        reversionVentaDTO.getIdVenta(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "La reversión de la venta no pudo completarse.");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/estadoVenta")
    public ResponseEntity<Object> actualizarEstadoVenta(@RequestBody EstadosDTO estadosDTO) {
        try {
            int idVenta = estadosDTO.getOperacion();
            for (articulosEstadoDTO estado : estadosDTO.getArticulos()) {
                ventaService.actualizarEstadoVenta(idVenta, estado);
            }
            Map<String, String> response = new HashMap<>();
            response.put("message", "Se cambió el estado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            errorLoggingService.logError("Error en VentaController - actualizarEstadoVenta", e, estadosDTO.toString());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detalleVentasCombinado")
    public ResponseEntity<List<DetalleVentaDTO>> getAllDetalleVentasCombinado() {
        try {
            List<DetalleVentaDTO> detalleVentaDTOs = ventaService.getAllDetalleVentasCombinado();
            return ResponseEntity.ok(detalleVentaDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

