package com.project.Project.project.controller;
import com.project.Project.project.model.Articulo;
import com.project.Project.project.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {
    @Autowired
    private ArticuloService articuloService;
    @PostMapping("/registrarArticulo")
    public String agregarArticulo(@RequestBody Articulo articulo) {
        Integer id = articuloService.guardarArticulo(articulo);
        return "Articulo agregado exitosamente";
    }

}