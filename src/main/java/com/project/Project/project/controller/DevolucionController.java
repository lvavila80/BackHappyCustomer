package com.project.Project.project.controller;

import com.project.Project.project.model.Devolucion;
import com.project.Project.project.service.DevolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/venta_devolucion")
public class DevolucionController {

    @Autowired
    private DevolucionService devolucionService;

    @PostMapping("/registerDevolucion")
    public ResponseEntity<String> registerDevolucion(@RequestBody Devolucion devolucion) {
        try {
            Optional<Devolucion> existingDevolucion = devolucionService.findByCorreo(devolucion.getCorreo());

            if (existingDevolucion.isPresent()) {
                if (existingDevolucion.get().getConfirmacion() == 1) {
                    return new ResponseEntity<>("Ya puede iniciar sesión.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("El mensaje de confirmación ya ha sido enviado.", HttpStatus.CONFLICT);
                }
            }

            return new ResponseEntity<>("Debe registrarse en el sistema.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocurrio un error durante el proceso", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
