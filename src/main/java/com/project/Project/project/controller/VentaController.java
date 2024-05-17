package com.project.Project.project.controller;

import com.project.Project.project.model.EstadosDTO;
import com.project.Project.project.model.ReversionVentaDTO;
import com.project.Project.project.model.VentaArticuloDTO;
import com.project.Project.project.model.articulosEstadoDTO;
import com.project.Project.project.service.ErrorLoggingService;
import com.project.Project.project.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            return new ResponseEntity<>("Venta registrada exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            errorLoggingService.logError("Error en VentaController - createVenta", e, ventaArticuloDTO.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la venta: " + e.getMessage());
        }
    }

    @PostMapping("/devolucionVenta")
    public ResponseEntity<String> revertirVenta(@Valid @RequestBody ReversionVentaDTO reversionVentaDTO) {
        try {
            String emailDestinatario = "raranda@ucatolica.edu.co";

            boolean exito = ventaService.revertirVenta(reversionVentaDTO, emailDestinatario);
            if (exito) {
                String mensaje = String.format("Artículos devueltos exitosamente. ID de la Venta: %d, Fecha y hora de la reversión: %s",
                        reversionVentaDTO.getIdVenta(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return new ResponseEntity<>(mensaje, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("La reversión de la venta no pudo completarse.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/estadoVenta")
    public ResponseEntity<String> actualizarEstadoVenta(@RequestBody EstadosDTO estadosDTO) {
        try {
            int idVenta = estadosDTO.getOperacion();
            for(articulosEstadoDTO estado : estadosDTO.getArticulos()){
                ventaService.actualizarEstadoVenta(idVenta,estado);
            }
            return new ResponseEntity<>("Se cambió el estado", HttpStatus.OK);
        } catch (Exception e) {
            errorLoggingService.logError("Error en VentaController - actualizarEstadoCompra", e, "");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
