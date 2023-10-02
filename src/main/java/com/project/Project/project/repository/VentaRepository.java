package com.project.Project.project.repository;

import com.project.Project.project.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query(value = "SELECT * FROM ventas WHERE cantidad > :cantidad", nativeQuery = true)
    List<Venta> findVentasByCantidadGreaterThan(int cantidad);
}
