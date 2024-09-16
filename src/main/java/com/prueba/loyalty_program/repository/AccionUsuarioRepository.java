package com.prueba.loyalty_program.repository;

import com.prueba.loyalty_program.entity.AccionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccionUsuarioRepository extends JpaRepository<AccionUsuario,Long> {
}
