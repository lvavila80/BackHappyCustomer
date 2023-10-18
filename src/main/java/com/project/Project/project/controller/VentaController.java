package com.project.Project.project.controller;

import com.project.Project.project.model.Venta;
import com.project.Project.project.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/nuevaVenta")
    public ResponseEntity<Object> createVenta(@RequestBody Venta venta) {
        try {
            Venta createdVenta = ventaService.createVenta(venta);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVenta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la venta: " + e.getMessage());
        }
    }
}
