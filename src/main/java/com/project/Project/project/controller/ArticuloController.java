package com.project.Project.project.controller;

import com.project.Project.project.model.Articulo;
import com.project.Project.project.model.ArticuloUpdateDTO;
import com.project.Project.project.model.ArticuloUpdateValorDTO;
import com.project.Project.project.repository.ArticuloRepository;
import com.project.Project.project.service.ArticuloService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private ArticuloRepository articuloRepository;

    @GetMapping("/todos")
    public ResponseEntity<List<Articulo>> obtenerTodosLosArticulos() {
        List<Articulo> articulos = articuloService.obtenerTodosLosArticulos();
        return ResponseEntity.ok(articulos);
    }

    @PostMapping("/registrarArticulo")
    public String agregarArticulo(@Valid @RequestBody Articulo articulo) {
        return "Los artículos deben crearse desde compras.";
    }

    @DeleteMapping("/eliminarArticulo")
    public ResponseEntity<String> eliminarArticulo(@RequestBody Map<String, Integer> requestBody) {
        try {
            int id = requestBody.get("id");
            if (articuloService.eliminarArticulo(id)) {
                return ResponseEntity.ok("Artículo eliminado exitosamente.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al intentar eliminar el artículo: " + e.getMessage());
        }
    }

    @PutMapping("/actualizarArticulo/{id}")
    public ResponseEntity<String> actualizarArticulo(@PathVariable Integer id, @Valid @RequestBody ArticuloUpdateDTO articuloUpdateDTO) {
        try {
            Articulo articulo = articuloUpdateDTO.getArticulo();
            Integer idCategoria = articuloUpdateDTO.getIdCategoria();
            articulo.setId(id);  // Asegurarse de que el artículo tenga el ID de la URL
            if (articuloService.actualizarArticulo(articulo, idCategoria)) {
                return ResponseEntity.ok("Artículo actualizado exitosamente.");
            } else {
                return ResponseEntity.badRequest().body("Error al actualizar el artículo.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al intentar actualizar el artículo: " + e.getMessage());
        }
    }


    @PutMapping("/updateValorUnitario")
    public ResponseEntity<String> updateValorUnitario(@Valid @RequestBody ArticuloUpdateValorDTO updateDTO) {
        if (articuloService.updateValorUnitario(updateDTO.getId(), updateDTO.getValorunitario())) {
            return ResponseEntity.ok("Valor unitario actualizado exitosamente.");
        } else {
            return ResponseEntity.badRequest().body("Error: ID de artículo no encontrado.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Articulo> obtenerArticuloPorId(@PathVariable int id) {
        try {
            Articulo articulo = articuloService.findArticuloById(id);
            return ResponseEntity.ok(articulo);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
