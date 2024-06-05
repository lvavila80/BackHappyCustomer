package com.project.Project.project.service;

import com.project.Project.project.model.AuthResponse;
import com.project.Project.project.model.LoginRequest;
import com.project.Project.project.model.Usuario;
import com.project.Project.project.model.UsuarioDAO;
import com.project.Project.project.service.*;
import com.project.Project.project.repository.UsuarioRepository;
import com.project.Project.project.security.JwtService;
import org.springframework.security.core.AuthenticationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service

public class UsuarioService implements UserDetails{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService() {
    }

    public UsuarioDAO getUsuarioById(int id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return convertToUsuarioDAO(usuario);
        } else {
            return null;
        }
    }


    public UsuarioDAO convertToUsuarioDAO(Usuario usuario) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.setId(usuario.getId());
        usuarioDAO.setCorreo(usuario.getCorreo());
        usuarioDAO.setCedula(usuario.getCedula());
        usuarioDAO.setEstado(usuario.getEstado());
        usuarioDAO.setCambiarClave(usuario.isCambiarClave());
        usuarioDAO.setFechaUltimoCambioClave(usuario.getFechaUltimoCambioClave());
        return usuarioDAO;
    }

    public UsuarioDAO insertarUsuario(Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioRepository.save(usuario);
            return convertToUsuarioDAO(nuevoUsuario);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo insertar el usuario: " + e.getMessage(), e);
        }
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPasswd()));
        UserDetails user=usuarioRepository.findByCorreo(request.getCorreo()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario inexistente."));
    }

    public AuthResponse validarUsuario(String correo, String passwd, Usuario usuario) {
        if (usuario.getEstado().equals("Preregistro")) {
            throw new RuntimeException("Debe confirmar su registro antes de autenticarse");
        }
        if (usuario.getEstado().equals("Inhabilitado")) {
            throw new RuntimeException("El usuario se ha inhabilitado.");
        }

        try {
            AuthResponse authResponse = login(new LoginRequest(correo, passwd));
            resetearIntentosFallidos(usuario); // Resetea los intentos si el inicio de sesión es exitoso
            return authResponse;
        } catch (AuthenticationException e) {
            incrementarIntentoFallido(usuario);
            if (usuario.getIntentosFallidos() >= 3) {
                usuario.setEstado("Inhabilitado");
                usuarioRepository.save(usuario);
                Integer token = tokenGenerator.generateToken();
                usuario.setToken(token);
                usuarioRepository.save(usuario);
                try {
                    emailService.sendSimpleMessage(correo, "Token Rehabilitación usuario", "Este es su token de recuperación de usuario, ingréselo en la aplicación: " + token);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Error al enviar correo de recuperación, consulte con el administrador");
                }
                throw new RuntimeException("El usuario se ha inhabilitado por intentos de sesión fallidos. Se ha enviado un token a su correo para habilitar su usuario");
            }
            throw e; // Relanza la excepción para mantener la traza de la autenticación fallida
        }
    }

    public void incrementarIntentoFallido(Usuario usuario) {
        usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
        if (usuario.getIntentosFallidos() >= 3) {
            usuario.setFechaBloqueo(new Date(System.currentTimeMillis() + 3600 * 1000)); // Bloqueo por 1 hora
            usuario.setCuentaBloqueada(true);
        }
        usuarioRepository.save(usuario);
    }

    public void resetearIntentosFallidos(Usuario usuario) {
        usuario.setIntentosFallidos(0);
        usuario.setCuentaBloqueada(false);
        usuario.setFechaBloqueo(null);
        usuarioRepository.save(usuario);
    }

    public void bloquearCuenta(Usuario usuario) {
        // Asumiendo que usuario tiene un campo llamado 'cuentaBloqueada'
        usuario.setCuentaBloqueada(true);
        usuarioRepository.save(usuario); // Guardar el estado actualizado en la base de datos
    }

    public String rehabilitarUsuario(Integer token, String correo, String nuevaContrasenia) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Verifica que el token coincida y el usuario esté inhabilitado
        if (!usuario.getToken().equals(token) || !usuario.getEstado().equals("Inhabilitado")) {
            return "Token de rehabilitación inválido o el usuario no está inhabilitado.";
        }

        // Codifica la nueva contraseña y actualiza el usuario
        usuario.setPasswd(passwordEncoder.encode(nuevaContrasenia));
        usuario.setEstado("Activo"); // Cambia el estado a Activo
        usuario.setIntentosFallidos(0);
        usuario.setFechaBloqueo(null); // Asegúrate de limpiar cualquier fecha de bloqueo
        usuarioRepository.save(usuario);

        return "Usuario rehabilitado y contraseña actualizada.";
    }


    public boolean validarContrasenaUsuario(String correo, String passwd) {
        Usuario usuario = obtenerUsuarioPorCorreo(correo);
        if (usuario != null) {
            return passwordEncoder.matches(passwd, usuario.getPassword());
        }
        return false;
    }

    @Transactional
    public boolean confirmarRegistro(String correo, Integer token) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Usuario Inexistente."));
        if(usuario.getToken().equals(token)) {
            if(usuario.getEstado().equals("Activo")){
                throw new IllegalArgumentException("El usuario ya se encuentra activo.");
            }
            usuario.setEstado("Activo");
            usuarioRepository.save(usuario);
            return true;
        } else {
            throw new IllegalArgumentException("Token incorrecto");
        }
    }

    public void correoRecuperacionContrasenia(String correo){
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);
        if(usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
            Integer token = tokenGenerator.generateToken();
            usuario.setToken(token);
            usuarioRepository.save(usuario);
            try{
                emailService.sendSimpleMessage(correo,"Token Recuperación de contraseña","Este es su token de recuperacion de contraseña, ingreselo en la aplicación: " + token);
            }catch(Exception e){
                throw new IllegalArgumentException("Error al enviar correo de recuperación, consulte con el administrador");
            }
        }
    }
    public String recuperarContrasenia(int numeroToken, String nuevaContrasenia) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findByToken(numeroToken);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setPasswd(passwordEncoder.encode(nuevaContrasenia));
            usuario.setEstado("Activo");
            usuario.setIntentosFallidos(0);
            usuarioRepository.save(usuario);

            return "Contraseña actualizada con éxito.";
        } else {
            throw new IllegalArgumentException("Token incorrecto");
        }
    }

    public String validarContrasena(String nuevaContrasenia) {
        if (nuevaContrasenia.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres.";
        }
        if (!nuevaContrasenia.matches(".*[A-Z].*")) {
            return "La contraseña debe tener al menos una letra mayúscula.";
        }
        if (!nuevaContrasenia.matches(".*[a-z].*")) {
            return "La contraseña debe tener al menos una letra minúscula.";
        }
        if (!nuevaContrasenia.matches(".*[0-9].*")) {
            return "La contraseña debe tener al menos un número.";
        }
        if (!nuevaContrasenia.matches(".*[!@#$%^&()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return "La contraseña debe tener al menos un símbolo.";
        }
        return "ok";
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

