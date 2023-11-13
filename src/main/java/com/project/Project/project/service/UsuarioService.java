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
                if(usuario.getEstado().equals("Preregistro")){
                    throw new RuntimeException("Debe confirmar su registro antes de autenticarse");
                }
                return true;
            }
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
}
