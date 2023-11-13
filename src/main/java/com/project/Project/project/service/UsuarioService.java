package com.project.Project.project.service;

import com.project.Project.project.model.Usuario;
import com.project.Project.project.model.UsuarioDAO;
import com.project.Project.project.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
            // Usar una función de validación de contraseñas al comparar
            return validarContrasena(passwd) && usuario.getPasswd().equals(passwd);
        }
        return false;
    }

    // Validar la estructura de la contraseña
    private boolean validarContrasena(String passwd) {
        if (passwd == null || passwd.length() < 15) {
            return false;
        }
        boolean tieneMayuscula = false;
        boolean tieneNumero = false;
        boolean tieneSimbolo = false;

        for (char c : passwd.toCharArray()) {
            if (Character.isUpperCase(c)) tieneMayuscula = true;
            else if (Character.isDigit(c)) tieneNumero = true;
            else if (!Character.isLetterOrDigit(c)) tieneSimbolo = true;

            if (tieneMayuscula && tieneNumero && tieneSimbolo) {
                return true;
            }
        }

        return tieneMayuscula && tieneNumero && tieneSimbolo;
    }
}

