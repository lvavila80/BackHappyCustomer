package com.project.Project.project.controller;

import com.project.Project.project.model.Categoria;
import com.project.Project.project.repository.CategoriaRepository;
import com.project.Project.project.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("/getusuario/{id}")
    public ResponseEntity<Categoria> getUsuarioById(@PathVariable int id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuariosNativo/{userId}")
    public List<Object[]> getALLCategoriasWithRol(@PathVariable int userId) {
        List<Object[]> usuariosConRol = usuarioRepository.findUsuariosWithRolId(userId);
        return usuariosConRol;
    }
}

