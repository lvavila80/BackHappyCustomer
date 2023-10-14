package com.project.Project.project.repository;
import com.project.Project.project.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {

    Optional<Articulo> findByNombrearticuloIgnoreCaseAndMarcaIgnoreCaseAndModeloIgnoreCaseAndColorIgnoreCase(
            String nombrearticulo,
            String marca,
            String modelo,
            String color);
}
