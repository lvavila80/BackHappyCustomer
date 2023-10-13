package com.project.Project.project.service;

import com.project.Project.project.model.Devolucion;
import com.project.Project.project.repository.DevolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevolucionService {

    @Autowired
    private DevolucionRepository devolucionRepository;

    // Método para buscar una Devolucion por correo
    public Optional<Devolucion> findByCorreo(String correo) {
        return devolucionRepository.findByCorreo(correo);
    }

    // Método para guardar una Devolucion
    public Devolucion save(Devolucion devolucion) {
        return devolucionRepository.save(devolucion);
    }

    // Método para obtener todas las Devoluciones
    public List<Devolucion> findAll() {
        return devolucionRepository.findAll();
    }

    // Método para obtener una Devolucion por su ID
    public Optional<Devolucion> findById(int id) {
        return devolucionRepository.findById(id);
    }

    // Método para eliminar una Devolucion por su ID
    public void deleteById(int id) {
        devolucionRepository.deleteById(id);
    }

    // ... Cualquier otro método que necesites para tu servicio
}
