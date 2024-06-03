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
            if (detalleCompraRepository.existsByIdarticulo(id)) {
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
            try {
                articuloRepository.save(existente);
            } catch (Exception e) {
                throw new RuntimeException("Error: " + e.getMessage());
            }
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
        try {
            Optional<Articulo> articuloOptional = articuloRepository.findById(id);
            if (articuloOptional.isPresent()) {
                Articulo articulo = articuloOptional.get();
                articulo.setValorunitario(valorunitario);
                try {
                    articuloRepository.save(articulo);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException("Error interno al guardar el articulo: " + e.getMessage());
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error: el articulo no existe.");
        }
    }

    @Transactional
    public Articulo findById(int idArticulo) {
        Optional<Articulo> optionalArticulo = articuloRepository.findById(idArticulo);

        if (optionalArticulo.isPresent()) {
            return optionalArticulo.get();
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
        if (articulo == null || articulo.getId() == null) {
            throw new IllegalArgumentException("El artículo o su ID no pueden ser nulos.");
        }

        Optional<Articulo> articuloExistente = articuloRepository.findById(articulo.getId());
        if (articuloExistente.isPresent()) {
            Articulo art = articuloExistente.get();
            art.setNombrearticulo(articulo.getNombrearticulo());
            art.setMarca(articulo.getMarca());
            art.setModelo(articulo.getModelo());
            art.setColor(articulo.getColor());
            art.setUnidadesdisponibles(articulo.getUnidadesdisponibles());
            art.setValorunitario(articulo.getValorunitario());
            articuloRepository.save(art);

            // Actualizar la relación con la categoría
            Optional<ArticuloCategoria> articuloCategoriaOpt = articuloCategoriaRepository.findByIdarticulo(art.getId());
            ArticuloCategoria articuloCategoria;
            if (articuloCategoriaOpt.isPresent()) {
                articuloCategoria = articuloCategoriaOpt.get();
            } else {
                articuloCategoria = new ArticuloCategoria();
                articuloCategoria.setIdarticulo(art.getId());
            }
            articuloCategoria.setIdcategoria(idCategoria);
            articuloCategoriaRepository.save(articuloCategoria);

            return true;
        } else {
            return false;
        }
    }

}

