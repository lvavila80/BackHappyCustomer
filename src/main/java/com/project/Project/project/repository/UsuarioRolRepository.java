package com.project.Project.project.repository;
import com.project.Project.project.model.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.Project.project.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Integer> {

}