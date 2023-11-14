package com.project.Project.project.service;

import com.project.Project.project.model.Usuario;
import com.project.Project.project.model.UsuarioDAO;
import com.project.Project.project.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private EmailService emailService;

    public UsuarioDAO getUsuarioById(int id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return convertToUsuarioDAO(usuario);
        } else {
            return null;
        }
    }

    public List<Object[]> getUsuariosWithRol(int userId) {
        return usuarioRepository.findUsuariosWithRolId(userId);
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

    public boolean validarUsuario(String correo, String passwd) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if(usuario.getPasswd().equals(passwd)){
                if(usuario.getIntentosFallidos() > 0 ){
                    usuario.setIntentosFallidos(0);
                    usuarioRepository.save(usuario);
                }
                if(usuario.getEstado().equals("Preregistro")){
                    throw new RuntimeException("Debe confirmar su registro antes de autenticarse");
                }
                if(usuario.getEstado().equals("Inhabilitado")){
                    throw new RuntimeException("El usuario se ha inhabilitado.");
                }
                return true;
            }else{
                usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                usuarioRepository.save(usuario);
                if(usuario.getIntentosFallidos()>=3){
                    usuario.setEstado("Inhabilitado");
                    usuarioRepository.save(usuario);
                    Integer token = tokenGenerator.generateToken();
                    usuario.setToken(token);
                    usuarioRepository.save(usuario);
                    try{
                        emailService.sendSimpleMessage(correo,"Token Rehabilitación usuario","Este es su token de recuperacion de usuario, ingreselo en la aplicación: " + token);
                    }catch(Exception e){
                        throw new IllegalArgumentException("Error al enviar correo de recuperación, consulte con el administrador");
                    }
                    throw new RuntimeException("El usuario se ha inhabilitado por intentos de sesion fallidos. Se ha enviado un token a su correo para habilitar su usuario");
                }
            }
        }else{
            throw new RuntimeException("Usuario Inexistente");
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
            if (nuevaContrasenia.length() < 8) {
                return "La contraseña debe tener al menos 8 caracteres.";
            }
            if (!nuevaContrasenia.matches("^(?=.[A-Z]).$")) {
                return "La contraseña debe tener al menos una letra mayúscula.";
            }
            if (!nuevaContrasenia.matches("^(?=.[a-z]).$")) {
                return "La contraseña debe tener al menos una letra minúscula.";
            }
            if (!nuevaContrasenia.matches("^(?=.[0-9]).$")) {
                return "La contraseña debe tener al menos un número.";
            }
            if (!nuevaContrasenia.matches("^(?=.[!@#$%^&()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$")) {
                return "La contraseña debe tener al menos un símbolo.";
            }
            Usuario usuario = usuarioOptional.get();
            usuario.setPasswd(nuevaContrasenia);
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
}

