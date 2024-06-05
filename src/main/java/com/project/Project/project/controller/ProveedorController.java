package com.project.Project.project.controller;

import com.project.Project.project.model.Proveedor;
import com.project.Project.project.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/createProveedor")
    public ResponseEntity<?> createProveedor(@RequestBody Proveedor proveedor) {
        Proveedor existingProveedor = proveedorRepository.findByIdentificacion(proveedor.getIdentificacion());
        if (existingProveedor != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El proveedor ya se encuentra registrado.");
        } else {
            Proveedor newProveedor = proveedorRepository.save(proveedor);
            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        }
    }

    @DeleteMapping("/deleteProveedor/{id}")
    public ResponseEntity<?> deleteProveedor(@PathVariable int id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById((long) id);
        if (proveedor.isPresent()) {
            proveedorRepository.deleteById((long) id);
            return ResponseEntity.ok().body("Proveedor eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Proveedor no encontrado.");
        }
    }

    @PutMapping("/updateProveedor/{id}")
    public ResponseEntity<?> updateProveedor(@PathVariable int id, @RequestBody Proveedor proveedor) {
        Optional<Proveedor> optionalProveedor = proveedorRepository.findById((long) id);
        if (optionalProveedor.isPresent()) {
            Proveedor existingProveedor = optionalProveedor.get();
            existingProveedor.setNombre(proveedor.getNombre());
            existingProveedor.setIdentificacion(proveedor.getIdentificacion());
            existingProveedor.setTelefono(proveedor.getTelefono());
            existingProveedor.setCorreo(proveedor.getCorreo());
            proveedorRepository.save(existingProveedor);
            return ResponseEntity.ok().body("Proveedor actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Proveedor no encontrado.");
        }
    }

}

