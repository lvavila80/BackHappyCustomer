package com.project.Project.project.controller;

import com.project.Project.project.model.CompraArticulosDTO;
import com.project.Project.project.service.CompraService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping("/registrarCompra")
    public ResponseEntity<String> agregarCompra( @RequestBody CompraArticulosDTO compraArticulosDTO) {
        try {
            compraService.guardarCompraYRelaciones(compraArticulosDTO);
            return new ResponseEntity<>("Compra y artículo agregados exitosamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/devolucionCompra")
    public ResponseEntity<String> actualizarDevolucion(@RequestBody DevoUpdateDTO devoUpdateDTO) {
        try {
            compraService.actualizarDevolucion(devoUpdateDTO.getIdCompra(), devoUpdateDTO.getDescripcion(), devoUpdateDTO.getDevuelto());
            return new ResponseEntity<>("Devolución actualizada exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Data
    public static class DevoUpdateDTO {
        private Integer idCompra;
        private String descripcion;
        private Boolean devuelto;
    }
}