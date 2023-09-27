package com.project.Project.project.controller;
import com.project.Project.project.model.Categoria;
import com.project.Project.project.repository.CategoriaRepository;
import com.project.Project.project.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostMapping("/createCategoria")
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria _categoria = categoriaRepository.save(new Categoria(categoria.getNombreCategorias()));
            return new ResponseEntity<>(_categoria, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}