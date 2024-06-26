package com.project.Project.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.Project.project.model.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreo(String correo);
    boolean existsByCedula(int cedula);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByToken(Integer token);
    Optional<Usuario> findByTokenAndCorreo(Integer token, String correo);
}


