package com.project.Project.project.controller;
import com.project.Project.project.model.Usuario;
import com.project.Project.project.model.UsuarioDAO;
import com.project.Project.project.model.UsuarioRol;
import com.project.Project.project.repository.UsuarioRepository;
import com.project.Project.project.repository.UsuarioRolRepository;
import com.project.Project.project.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

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

    @PostMapping("/insertar")
    public ResponseEntity<Map<String, Object>> insertarUsuario(@RequestBody Map<String, Object> usuarioData) {
        Map<String, Object> response = new HashMap<>();

        String correo = (String) usuarioData.get("correo");
        String passwd = (String) usuarioData.get("passwd");
        int cedula = (int) usuarioData.get("cedula");
        String nombre = (String) usuarioData.get("nombre");
        String estado = (String) usuarioData.get("estado");
        boolean cambiarClave = (boolean) usuarioData.get("cambiarClave");
        Date fechaUltimoCambioClave = new Date();

        int idRol = (int) usuarioData.get("idRol");

        if (usuarioRepository.existsByCorreo(correo)) {
            response.put("error", "El correo ya está en uso.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (usuarioRepository.existsByCedula(cedula)) {
            response.put("error", "La cédula ya está en uso.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            Usuario usuario = new Usuario(correo, passwd, cedula, nombre, estado, cambiarClave, fechaUltimoCambioClave);
            UsuarioDAO nuevoUsuario = usuarioService.insertarUsuario(usuario);

            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setIdUsuario(nuevoUsuario.getId());
            usuarioRol.setIdRol(idRol);
            usuarioRolRepository.save(usuarioRol);

            response.put("message", "Usuario insertado con éxito. ID: " + nuevoUsuario.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("error", "No se pudo insertar el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}