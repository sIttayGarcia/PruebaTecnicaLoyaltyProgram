package com.prueba.loyalty_program.repository;

import com.prueba.loyalty_program.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUserName(String userName);

    Optional<Usuario> findById(Long id);


}
