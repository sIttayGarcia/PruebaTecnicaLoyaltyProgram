package com.prueba.loyalty_program.repository;

import com.prueba.loyalty_program.entity.Accion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccionRepository extends JpaRepository<Accion, Long> {
    Optional<Accion> findByNombre(String nombre);
    Optional<Accion> findById(Long id);


}
