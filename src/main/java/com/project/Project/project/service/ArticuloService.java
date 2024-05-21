package com.project.Project.project.service;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.ArticuloRepository;
import com.project.Project.project.repository.ArticuloCategoriaRepository;
import com.project.Project.project.repository.CompraRepository;
import com.project.Project.project.repository.DetalleCompraRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArticuloService {
    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ArticuloCategoriaRepository articuloCategoriaRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    public List<Articulo> obtenerTodosLosArticulos() {
        return articuloRepository.findAll();
    }


    public Articulo findArticuloById(int idArticulo) throws EntityNotFoundException {
        return articuloRepository.findById(idArticulo)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el artículo con ID " + idArticulo));
    }

    @Transactional
    public boolean eliminarArticulo(int id) {
        try {
            if(detalleCompraRepository.existsByIdarticulo(id)){
                detalleCompraRepository.deleteByIdarticulo(id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error detalleCompra: " + e.getMessage());
        }
        try {
            articuloCategoriaRepository.eliminarPorIdArticulo(id);
        } catch (Exception e) {
            throw new RuntimeException("Error articuloCategoria: " + e.getMessage());
        }

        try {
            Optional<Articulo> articuloOptional = articuloRepository.findById(id);
            if (articuloOptional.isPresent()) {
                articuloRepository.delete(articuloOptional.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error articulo: " + e.getMessage());
        }
    }


    public Integer guardarArticulo(ArticulosCompraDTO compraDTO) {
        Articulo articulo = compraDTO.getArticulo();
        Optional<Articulo> articuloExistente = articuloRepository.findByNombrearticuloIgnoreCaseAndMarcaIgnoreCaseAndModeloIgnoreCaseAndColorIgnoreCase(
                articulo.getNombrearticulo(),
                articulo.getMarca(),
                articulo.getModelo(),
                articulo.getColor()
        );

        if (articuloExistente.isPresent()) {
            Articulo existente = articuloExistente.get();
            int totalUnidades = existente.getUnidadesdisponibles() + compraDTO.getUnidadesCompradas();

            double valorPromedioPonderado = ((existente.getValorunitario() * existente.getUnidadesdisponibles())
                    + (compraDTO.getValorUnidad() + (compraDTO.getValorUnidad() * 0.25)) * compraDTO.getUnidadesCompradas())
                    / totalUnidades;

            existente.setValorunitario(valorPromedioPonderado);
            existente.setUnidadesdisponibles(totalUnidades);
            try{
                articuloRepository.save(existente);
            }catch (Exception e) {
                throw new RuntimeException("Error: " + e.getMessage());
            };
            return existente.getId();
        } else {
            // Si no existe, guarda el nuevo artículo y devuelve su ID.
            double nuevoValor = compraDTO.getValorUnidad() + (compraDTO.getValorUnidad() * 0.25);
            articulo.setValorunitario(nuevoValor);
            articulo.setUnidadesdisponibles(compraDTO.getUnidadesCompradas());
            Articulo nuevoArticulo = articuloRepository.save(articulo);
            try {
                compraRepository.insertArticuloCategoria(nuevoArticulo.getId(), compraDTO.getIdCategoria());
            } catch (Exception e) {
                throw new RuntimeException("Error al insertar en bd.articulo_categoria: " + e.getMessage());
            }
            return nuevoArticulo.getId();
        }
    }
    public boolean updateValorUnitario(Integer id, double valorunitario) {
        try{
            Optional<Articulo> articuloOptional = articuloRepository.findById(id);
            if (articuloOptional.isPresent()) {
                Articulo articulo = articuloOptional.get();
                articulo.setValorunitario(valorunitario);
                try{
                    articuloRepository.save(articulo);
                    return true;
                }catch(Exception e){
                    throw new RuntimeException("Error,interno al guardar el articulo ");
                }
            }
            return false;
        }catch (Exception e){
            throw new RuntimeException("Error,el articulo no existe: ");
        }
    }

    @Transactional
    public Articulo findById(int idArticulo) {
        Optional<Articulo> optionalArticulo = articuloRepository.findById(idArticulo);

        if (optionalArticulo.isPresent()) {
            Articulo articulo = optionalArticulo.get();
            return articulo;
        } else {
            throw new RuntimeException("No se encontró el artículo con ID " + idArticulo);
        }
    }

    @Transactional
    public void findByIdAndUpdateUnidadesDisponibles(Integer idArticulo, int nuevasUnidades) {
        Optional<Articulo> optionalArticulo = articuloRepository.findById(idArticulo);

        if (optionalArticulo.isPresent()) {
            Articulo articulo = optionalArticulo.get();
            articulo.setUnidadesdisponibles(nuevasUnidades);
            articuloRepository.save(articulo);
        } else {
            throw new RuntimeException("No se encontró el artículo con ID " + idArticulo);
        }
    }

    @Transactional
    public boolean actualizarArticulo(Articulo articulo, Integer idCategoria) {
        try {
            Articulo articuloExistente = articuloRepository.findById(articulo.getId())
                    .orElseThrow(() -> new RuntimeException("No se encontró el artículo con ID " + articulo.getId()));

            articuloExistente.setNombrearticulo(articulo.getNombrearticulo());
            articuloExistente.setMarca(articulo.getMarca());
            articuloExistente.setModelo(articulo.getModelo());
            articuloExistente.setColor(articulo.getColor());
            articuloExistente.setUnidadesdisponibles(articulo.getUnidadesdisponibles());
            articuloExistente.setValorunitario(articulo.getValorunitario());
            articuloRepository.save(articuloExistente);

            ArticuloCategoria articuloCategoria = articuloCategoriaRepository.findByIdarticulo(articulo.getId())
                    .orElseThrow(() -> new RuntimeException("No se encontró la categoría del artículo con ID " + articulo.getId()));
            articuloCategoria.setIdcategoria(idCategoria);
            articuloCategoriaRepository.save(articuloCategoria);

            List<DetalleCompra> detallesCompra = detalleCompraRepository.findByIdarticulo(articulo.getId());
            detallesCompra.forEach(detalleCompra -> {
                detalleCompra.setUnidadescompradas(articulo.getUnidadesdisponibles());
                detalleCompra.setValorunidad(articulo.getValorunitario());
                detalleCompraRepository.save(detalleCompra);
            });
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al intentar actualizar el artículo: " + e.getMessage(), e);
        }
    }



}
