package com.project.Project.project.service;
import java.util.ArrayList;
import java.util.List;

import com.project.Project.project.model.Articulo;
import com.project.Project.project.model.ArticulosCompraDTO;
import com.project.Project.project.model.Compra;
import com.project.Project.project.model.DetalleCompra;
import com.project.Project.project.repository.ArticuloRepository;
import com.project.Project.project.repository.CompraRepository;
import com.project.Project.project.repository.DetalleCompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
public class DetalleCompraService {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    public List<DetalleCompra> getDetallesCompraByIdcompra(int idcompra) {
        return detalleCompraRepository.findByIdcompra(idcompra);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public DetalleCompra guardarDetalleCompra(ArticulosCompraDTO compraArticulosDTO,Compra savedCompra, int idArticulo) {
        try {
            DetalleCompra detalleCompra = new DetalleCompra();
            detalleCompra.setIdcompra(savedCompra.getId());
            detalleCompra.setIdcategoria(compraArticulosDTO.getIdCategoria());
            detalleCompra.setValorunidad(compraArticulosDTO.getValorUnidad());
            detalleCompra.setUnidadescompradas(compraArticulosDTO.getUnidadesCompradas());
            detalleCompra.setIdarticulo(idArticulo);
            return detalleCompraRepository.save(detalleCompra);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar compra y relaciones: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<String> reversarCompra(int idcompra, ArrayList array,String detalleDevolucion){
        List<DetalleCompra> detalles = detalleCompraRepository.findByIdcompra(idcompra);
        for (DetalleCompra detalle : detalles) {
            System.out.println();
            try {
                if(detalle.getEstado().equals("devuelto") && array.contains(detalle.getIdarticulo())){
                    return new ResponseEntity<>("El articulo " + detalle.getIdarticulo() + " ya se encuentra devuelto", HttpStatus.INTERNAL_SERVER_ERROR);
                }else if(array.contains(detalle.getIdarticulo()) && !detalle.getEstado().equals("devuelto")) {
                    detalle.setEstado("devuelto");
                    detalle.setDetalleDevolucion(detalleDevolucion);
                    detalleCompraRepository.save(detalle);
                    Articulo articulo = articuloRepository.findById(detalle.getIdarticulo()).get();
                    int nuevasUnidades = ((articulo.getUnidadesdisponibles())-(detalle.getUnidadescompradas()));
                    articuloRepository.updateUnidadesDisponiblesById(detalle.getIdarticulo(), nuevasUnidades);
                }else{
                    return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("Compra y artículo agregados exitosamente", HttpStatus.OK);
    }
}