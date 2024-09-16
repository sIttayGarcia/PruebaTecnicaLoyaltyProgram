package com.prueba.loyalty_program.repository;

import com.prueba.loyalty_program.entity.Recompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa,Long> {
    @Override
    Optional<Recompensa> findById(Long id);

    Optional<Recompensa> findByNombre(String nombre);
}
