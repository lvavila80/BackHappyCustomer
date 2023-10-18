package com.project.Project.project.service;
import java.util.List;
import com.project.Project.project.model.ArticulosCompraDTO;
import com.project.Project.project.model.Compra;
import com.project.Project.project.model.DetalleCompra;
import com.project.Project.project.repository.DetalleCompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.Project.project.model.DetalleCompra;
@Service
public class DetalleCompraService {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ArticuloService articuloService;

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

        List<DetalleCompra> detalles = detalleCompraRepository.findByIdcompra(idcompra);

        // Procesar cada DetalleCompra
        for (DetalleCompra detalle : detalles) {
            // Aquí puedes hacer lo que necesites con cada DetalleCompra
            // Por ejemplo, imprimir la información:
            System.out.println("ID: " + detalle.getId());
            System.out.println("ID Compra: " + detalle.getIdcompra());
            System.out.println("ID Artículo: " + detalle.getIdarticulo());
            System.out.println("Unidades compradas: " + detalle.getUnidadescompradas());
            System.out.println("Valor unidad: " + detalle.getValorunidad());
            //... y así sucesivamente para los demás campos.

            // También puedes hacer otras operaciones, como actualizar datos, realizar cálculos, etc.
        }

        return detalles;  // Devuelve la lista procesada (aunque no la hemos modificado en este ejemplo)
    }
}

}