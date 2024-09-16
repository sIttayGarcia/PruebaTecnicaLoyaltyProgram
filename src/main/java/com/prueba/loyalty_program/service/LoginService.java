package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.LoginDTO;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.model.ResponseLogin;
import com.prueba.loyalty_program.repository.UsuarioRepository;
import com.prueba.loyalty_program.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseLogin login(LoginDTO loginRequestDTO) throws Exception {
        // Buscar al usuario por email o username
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginRequestDTO.getUserNameOrEmail());

        if (!optionalUsuario.isPresent()) {
            optionalUsuario = usuarioRepository.findByUserName(loginRequestDTO.getUserNameOrEmail());
        }

        Usuario usuario = optionalUsuario.orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Validar la contraseña
        if (!BCrypt.checkpw(loginRequestDTO.getPassword(), usuario.getPassword())) {
            throw new Exception("Contraseña incorrecta");
        }

        // Generar el token JWT
        String token = jwtUtil.generateToken(usuario.getUserName());

        // Devolver el token junto con el mensaje de éxito
        return new ResponseLogin("Inicio de sesión exitoso", token);
    }
}
