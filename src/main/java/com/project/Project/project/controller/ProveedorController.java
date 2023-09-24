package com.project.Project.project.controller;

import com.project.Project.project.model.Proveedor;
import com.project.Project.project.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable int id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById((long) id);
        if (proveedor.isPresent()) {
            return ResponseEntity.ok(proveedor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    @PostMapping("/create")
    public Proveedor createProveedor(@RequestBody Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Proveedor> updateProveedor(@PathVariable int id, @RequestBody Proveedor proveedorDetalles) {
        Optional<Proveedor> proveedor = proveedorRepository.findById((long) id);

        if (proveedor.isPresent()) {
            Proveedor proveedorExistente = proveedor.get();
            proveedorExistente.setNombre(proveedorDetalles.getNombre());
            proveedorExistente.setIdentificacion(proveedorDetalles.getIdentificacion());
            proveedorExistente.setTelefono(proveedorDetalles.getTelefono());
            proveedorExistente.setCorreo(proveedorDetalles.getCorreo());

            return ResponseEntity.ok(proveedorRepository.save(proveedorExistente));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProveedor(@PathVariable int id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById((long) id);

        if (proveedor.isPresent()) {
            proveedorRepository.delete(proveedor.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
