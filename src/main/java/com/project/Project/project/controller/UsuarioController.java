package com.project.Project.project.controller;

import com.project.Project.project.model.*;
import com.project.Project.project.repository.UsuarioRepository;
import com.project.Project.project.security.JwtService;
import com.project.Project.project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenGenerator tokenGenerator;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/getusuario/{id}")
    public ResponseEntity<ApiResponse> getUsuarioById(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Usuario encontrado"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Usuario no encontrado"));
        }
    }


    @PostMapping("/insertarUsuario")
    public ResponseEntity<ApiResponse> insertarUsuario(@RequestBody Map<String, Object> usuarioData) {
        String correo = (String) usuarioData.get("correo");
        String passwd = (String) usuarioData.get("passwd");
        int cedula = (int) usuarioData.get("cedula");
        String nombre = (String) usuarioData.get("nombre");
        boolean cambiarClave = (boolean) usuarioData.get("cambiarClave");
        Date fechaUltimoCambioClave = new Date();
        String roleString = String.valueOf(usuarioData.get("rol")).toUpperCase(); // Corregido
        Role rol = Role.valueOf(roleString);
        if (((String) usuarioData.get("rol")).equals("ADMIN")) {
            rol = Role.ADMIN;
        } else if (((String) usuarioData.get("rol")).equals("OPERATIVO")) {
            rol = Role.OPERATIVO;
        } else if (((String) usuarioData.get("rol")).equals("AUDITOR")) {
            rol = Role.AUDITOR;
        } else {
            rol = Role.OPERATIVO;
        }

        if (usuarioRepository.existsByCorreo(correo)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "El correo ya está en uso."));
        }

        if (usuarioRepository.existsByCedula(cedula)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "La cédula ya está en uso."));
        }

        String valid = usuarioService.validarContrasena(passwd);
        if (!(valid.equals("ok"))) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "No se pudo insertar el usuario: "));
        }
        int token = tokenGenerator.generateToken();
        try {
            emailService.sendSimpleMessage(correo, "Token Registro Gestion de Inventarios", "Este es su token de confirmación de registro, ingreselo en la aplicación: " + token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "No se pudo insertar el usuario: " + e.getMessage()));
        }


        Usuario user = Usuario.builder()
                .correo(correo)
                .passwd(passwordEncoder.encode(passwd))
                .cedula(cedula)
                .nombre(nombre)
                .estado("Preregistro")
                .intentosFallidos(0)
                .cambiarClave(cambiarClave)
                .fechaUltimoCambioClave(fechaUltimoCambioClave)
                .token(tokenGenerator.generateToken())
                .rol(rol)
                .build();
        usuarioService.insertarUsuario(user);
        try {
            emailService.sendSimpleMessage(correo, "Token Registro Gestión de Inventarios", "Este es su token de confirmación de registro, ingréselo en la aplicación: " + user.getToken());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error al enviar correo" + ex));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Registro exitoso. Verifique su correo electrónico para confirmar su registro."));
    }


    @PostMapping("/authUsuario")

    public ResponseEntity<ApiResponse> validarUsuario(@RequestBody LoginRequest request) {
        try {
            String correo = request.getCorreo();
            String passwd = request.getPasswd();
            Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Usuario no encontrado."));
            }

            if (usuario.isCuentaBloqueada()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Cuenta bloqueada. Intenta de nuevo más tarde."));
            }

            AuthResponse authResponse = usuarioService.validarUsuario(correo, passwd, usuario);

            if (authResponse != null) {
                usuarioService.resetearIntentosFallidos(usuario);
                return ResponseEntity.ok(new ApiResponse(true, "Usuario Autenticado. Token: " + authResponse.getToken()));
            } else {
                usuarioService.incrementarIntentoFallido(usuario);
                if (usuario.getIntentosFallidos() >= 3) {
                    usuarioService.bloquearCuenta(usuario);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Usuario inhabilitado por contraseña incorrecta."));
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Credenciales inválidas."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Error: " + e.getMessage()));
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

    @PostMapping("/correoReestablecerContrasenia")
    public ResponseEntity<String> recuperarContrasenia(@RequestBody Map<String, String> body){
        String correo= body.get("correo");
        if(correo!= null){
            try{
                usuarioService.correoRecuperacionContrasenia(correo);
                return ResponseEntity.ok("Se ha enviado un correo de recuperacion con su token a su dirección de email registrada.");
            }catch(Exception e){
                return ResponseEntity.badRequest().body("Error: "+ e);
            }
        }else{
            return ResponseEntity.badRequest().body("Correo inexistente.");
        }
    }

    @PostMapping("/ReestablecerContrasenia/{numeroToken}")
    public ResponseEntity<Map<String, String>> reestablecerContrasenia(@PathVariable("numeroToken") int numeroToken, @RequestBody Map<String, String> body) {
        String contrasenia = body.get("contrasenia");
        Map<String, String> response = new HashMap<>();

        try {
            if (String.valueOf(numeroToken).length() == 6) {
                String resultado = usuarioService.recuperarContrasenia(numeroToken, contrasenia);
                response.put("message", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "El token debe ser de seis dígitos.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/RehabilitarUsuario/{token}")
    public ResponseEntity<ApiResponse> rehabilitarUsuario(
            @PathVariable("token") Integer token,
            @RequestBody Map<String, String> body) {

        String correo = body.get("correo");
        String contrasenia = body.get("contrasenia");

        if (correo == null || contrasenia == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Correo y/o contraseña no proporcionados."));
        }

        try {
            String resultado = usuarioService.rehabilitarUsuario(token, correo, contrasenia);
            return ResponseEntity.ok(new ApiResponse(true, resultado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Error: " + e.getMessage()));
        }
    }
}