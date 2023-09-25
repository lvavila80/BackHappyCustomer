package com.project.Project.project.repository;
import com.project.Project.project.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.Project.project.model.Categoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @Query(value = "SELECT c.categoria " +
            "FROM categorias c " +
            "JOIN articulo_categoria ac ON c.id = ac.idcategoria " +
            "WHERE ac.idarticulo = :articuloId", nativeQuery = true)
    default List<Object[]> findCategoriaWithRolId(@Param("userId") int userId) {
        return null;
    }

}