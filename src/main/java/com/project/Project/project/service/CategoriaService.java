
package com.project.Project.project.service;
import com.project.Project.project.model.Categoria;
import com.project.Project.project.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public ResponseEntity<?> createCategoria(Categoria categoria) {
        try {
            Optional<Categoria> existingCategoria = categoriaRepository.findByNombreCategorias(categoria.getNombreCategorias());
            if (existingCategoria.isPresent()) {
                return ResponseEntity.badRequest().body("Error creating Categoria: Categoria with name " + categoria.getNombreCategorias() + " already exists");
            }
            Categoria newCategoria = categoriaRepository.save(categoria);
            return ResponseEntity.ok(newCategoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating Categoria: " + e.getMessage());
        }
    }

    public ResponseEntity<?> get(int id) {

        return null;
    }
}

