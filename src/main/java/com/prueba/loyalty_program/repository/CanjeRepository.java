package com.prueba.loyalty_program.repository;

import com.prueba.loyalty_program.entity.Canje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanjeRepository extends JpaRepository<Canje,Long> {

    List<Canje> findByUsuarioId(Long usuarioId);
}
