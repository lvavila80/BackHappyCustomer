package com.project.Project.project.repository;


import com.project.Project.project.model.ArticuloCategoria;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticuloCategoriaRepository extends JpaRepository<ArticuloCategoria, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ArticuloCategoria ac WHERE ac.idarticulo = ?1")
    void eliminarPorIdArticulo(int idArticulo);

    @Query("SELECT ac FROM ArticuloCategoria ac WHERE ac.idarticulo = :idarticulo")
    Optional<ArticuloCategoria> findByIdarticulo(@Param("idarticulo") int idarticulo);

}