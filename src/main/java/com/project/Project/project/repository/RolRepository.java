package com.project.Project.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.project.Project.project.model.Rol;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}

