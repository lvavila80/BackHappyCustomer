package com.project.Project.project.service;

import com.project.Project.project.model.Articulo;
import com.project.Project.project.model.Compra;
import com.project.Project.project.model.CompraArticuloDTO;
import com.project.Project.project.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticuloService {
    @Autowired
    private ArticuloRepository articuloRepository;

    public Integer guardarArticulo(CompraArticuloDTO compraDTO) {
            Articulo articulo = compraDTO.getArticulo();
            Compra compra = compraDTO.getCompra();
        // Intenta encontrar un artículo existente con los mismos atributos.
        Optional<Articulo> articuloExistente = articuloRepository.findByNombrearticuloIgnoreCaseAndMarcaIgnoreCaseAndModeloIgnoreCaseAndColorIgnoreCase(
                articulo.getNombrearticulo(),
                articulo.getMarca(),
                articulo.getModelo(),
                articulo.getColor()
        );

        if (articuloExistente.isPresent()) {
            // Si existe, actualiza el número de unidades disponibles y guarda el artículo existente.
            Articulo existente = articuloExistente.get();
            int totalUnidades = existente.getUnidadesdisponibles() + compra.getUnidadescompradas();

            double valorPromedioPonderado = ((existente.getValorunitario() * existente.getUnidadesdisponibles())
                    + (compra.getValorunidad() + (compra.getValorunidad() * 0.25)) * compra.getUnidadescompradas())
                    / totalUnidades;

            existente.setValorunitario(valorPromedioPonderado);
            existente.setUnidadesdisponibles(totalUnidades);
            articuloRepository.save(existente);
            return existente.getId();
        } else {
            // Si no existe, guarda el nuevo artículo y devuelve su ID.
            double nuevoValor = compra.getValorunidad() + (compra.getValorunidad() * 0.25);
            articulo.setValorunitario(nuevoValor);
            articulo.setUnidadesdisponibles(compra.getUnidadescompradas());
            Articulo nuevoArticulo = articuloRepository.save(articulo);
            return nuevoArticulo.getId();
        }
    }
    public boolean updateValorUnitario(Integer id, double valorunitario) {
        Optional<Articulo> articuloOptional = articuloRepository.findById(id);
        if (articuloOptional.isPresent()) {
            Articulo articulo = articuloOptional.get();
            articulo.setValorunitario(valorunitario);
            articuloRepository.save(articulo);
            return true;
        }
        return false;
    }
}
