package com.project.Project.project.repository;

import com.project.Project.project.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Modifying
    @Query(value = "INSERT INTO venta_usuario (idventa, idusuario) VALUES (:idVenta, :idUsuario)", nativeQuery = true)
    void insertVentaUsuario(@Param("idVenta") Long idVenta, @Param("idUsuario") Long idUsuario);

    @Modifying
    @Query(value = "INSERT INTO venta_cliente (idventa, idcliente) VALUES (:idVenta, :idCliente)", nativeQuery = true)
    void insertVentaCliente(@Param("idVenta") Long idVenta, @Param("idCliente") Long idCliente);

    @Modifying
    @Query(value = "INSERT INTO venta_categoria (idventa, idcategoria) VALUES (:idVenta, :idCategoria)", nativeQuery = true)
    void insertVentaCategoria(@Param("idVenta") Long idVenta, @Param("idCategoria") Long idCategoria);

}
