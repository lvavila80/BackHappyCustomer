package com.project.Project.project.repository;

import com.project.Project.project.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

}
