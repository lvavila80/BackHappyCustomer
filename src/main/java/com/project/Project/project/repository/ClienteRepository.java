package com.project.Project.project.repository;
import com.project.Project.project.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
