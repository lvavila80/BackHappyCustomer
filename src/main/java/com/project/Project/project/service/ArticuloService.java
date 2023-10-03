package com.project.Project.project.service;

import com.project.Project.project.model.Articulo;
import com.project.Project.project.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticuloService {
    @Autowired
    private ArticuloRepository articuloRepository;

    public Integer guardarArticulo(Articulo articulo) {

        // Intenta encontrar un artículo existente con los mismos atributos.
        Optional<Articulo> articuloExistente = articuloRepository.findByNombrearticuloIgnoreCaseAndMarcaIgnoreCaseAndModeloIgnoreCaseAndColorIgnoreCase(
                articulo.getNombrearticulo(),
                articulo.getMarca(),
                articulo.getModelo(),
                articulo.getColor()
        );

        if (articuloExistente.isPresent()) {
            // Si existe, actualiza el número de unidades disponibles y guarda el artículo existente.
            Articulo existente = articuloExistente.get();
            existente.setUnidadesdisponibles(existente.getUnidadesdisponibles() + articulo.getUnidadesdisponibles());
            articuloRepository.save(existente);
            return existente.getId();
        } else {
            // Si no existe, guarda el nuevo artículo y devuelve su ID.
            Articulo nuevoArticulo = articuloRepository.save(articulo);
            return nuevoArticulo.getId();
        }
    }
}
