package com.project.Project.project.repository;
import com.project.Project.project.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.Project.project.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    @Modifying// Esta anotación indica que es una consulta que modifica datos
    @Query(value = "INSERT INTO bd.compra_articulo (idarticulo, idcompra) VALUES (:idArticulo, :idCompra)", nativeQuery = true)
    void insertCompraArticulo(@Param("idArticulo") Integer idArticulo, @Param("idCompra") Integer idCompra);

    @Modifying
    @Query(value = "INSERT INTO bd.compra_categoria (idcompra, idcategoria) VALUES (:idCompra, :idCategoria)", nativeQuery = true)
    void insertCompraCategoria(@Param("idCompra") Integer idCompra, @Param("idCategoria") Integer idCategoria);

    @Modifying
    @Query(value = "INSERT INTO bd.compra_proveedor (idproveedor, idcompra) VALUES (:idProveedor, :idCompra)", nativeQuery = true)
    void insertCompraProveedor(@Param("idProveedor") Integer idProveedor, @Param("idCompra") Integer idCompra);

    @Modifying
    @Query(value = "INSERT INTO bd.compra_usuario (idcompra, idusuario) VALUES (:idCompra, :idUsuario)", nativeQuery = true)
    void insertCompraUsuario(@Param("idCompra") Integer idCompra, @Param("idUsuario") Integer idUsuario);

    @Modifying
    @Query(value = "INSERT INTO bd.articulo_categoria (idarticulo, idcategoria) VALUES (:idArticulo, :idCategoria)", nativeQuery = true)
    void insertArticuloCategoria(@Param("idArticulo") Integer idArticulo, @Param("idCategoria") Integer idCategoria);

}