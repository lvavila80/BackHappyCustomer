package com.project.Project.project.controller;
import com.project.Project.project.model.Usuario;
import com.project.Project.project.model.UsuarioDAO;
import com.project.Project.project.model.UsuarioRol;
import com.project.Project.project.repository.UsuarioRepository;
import com.project.Project.project.repository.UsuarioRolRepository;
import com.project.Project.project.service.TokenGenerator;
import com.project.Project.project.service.TokenGenerator;
import com.project.Project.project.service.UsuarioService;
import com.project.Project.project.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenGenerator tokenGenerator;

    @GetMapping("/getusuario/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/usuariosNativo/{userId}")
    public List<Object[]> getUsuariosWithRol(@PathVariable int userId) {
        List<Object[]> usuariosConRol = usuarioRepository.findUsuariosWithRolId(userId);
        return usuariosConRol;
    }

    @PostMapping("/insertarUsuario")
    public ResponseEntity<String> insertarUsuario(@RequestBody Map<String, Object> usuarioData) {

        String correo = (String) usuarioData.get("correo");
        String passwd = (String) usuarioData.get("passwd");
        int cedula = (int) usuarioData.get("cedula");
        String nombre = (String) usuarioData.get("nombre");
        boolean cambiarClave = (boolean) usuarioData.get("cambiarClave");
        Date fechaUltimoCambioClave = new Date();

        int idRol = (int) usuarioData.get("idRol");

        if (usuarioRepository.existsByCorreo(correo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya está en uso.");
        }

        if (usuarioRepository.existsByCedula(cedula)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La cédula ya está en uso.");
        }
        int token = tokenGenerator.generateToken();
        try{
            emailService.sendSimpleMessage(correo,"Registro gestion de inventarios","Este es su token de confirmación de registro, ingreselo en la aplicación: " + token);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo insertar el usuario: " + e.getMessage());
        }

        try {
            Usuario usuario = new Usuario(correo, passwd, cedula, nombre, cambiarClave, fechaUltimoCambioClave, token);
            UsuarioDAO nuevoUsuario = usuarioService.insertarUsuario(usuario);

            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setIdUsuario(nuevoUsuario.getId());
            usuarioRol.setIdRol(idRol);
            usuarioRolRepository.save(usuarioRol);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario insertado con éxito. ID:" + nuevoUsuario.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo insertar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/authUsuario")
    public ResponseEntity<String> validarUsuario(@RequestBody Map<String, Object> credenciales) {
        try {
            String correo = (String) credenciales.get("correo");
            String passwd = (String) credenciales.get("passwd");

            if (usuarioService.validarUsuario(correo, passwd)) {
                return ResponseEntity.ok("Usuario Autenticado.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor: " + e.getMessage());
        }
    }

    @PostMapping("/confirmarregistro")
    public ResponseEntity<String> confirmarRegistro(@RequestBody Map<String, Object> credenciales) {
        try {
            String correo = (String) credenciales.get("correo");
            Integer token = (Integer) credenciales.get("token");

            if (usuarioService.confirmarRegistro(correo, token)) {
                return ResponseEntity.ok("Usuario Confirmado.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error " + e.getMessage());
        }
    }
}