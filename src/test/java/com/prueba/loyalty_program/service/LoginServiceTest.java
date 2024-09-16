package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.LoginDTO;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.model.ResponseLogin;
import com.prueba.loyalty_program.repository.UsuarioRepository;
import com.prueba.loyalty_program.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService; // Inyecta el servicio que vamos a probar

    @Mock
    private UsuarioRepository usuarioRepository; // Simula el repositorio

    @Mock
    private JwtUtil jwtUtil; // Simula la utilidad JWT

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    public void testLoginExitoso() throws Exception {
        // Datos de prueba
        String userName = "juanperez";
        String password = "miPasswordSegura";
        String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario usuario = new Usuario();
        usuario.setUserName(userName);
        usuario.setPassword(encodedPassword);

        // Mockear el comportamiento del repositorio
        when(usuarioRepository.findByUserName(userName)).thenReturn(Optional.of(usuario));

        // Mockear el comportamiento de la generación del token JWT
        when(jwtUtil.generateToken(userName)).thenReturn("fake-jwt-token");

        // Ejecutar el servicio de login
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setUserNameOrEmail(userName);
        loginRequest.setPassword(password);

        ResponseLogin response = loginService.login(loginRequest);

        // Verificar que el login fue exitoso
        assertEquals("Inicio de sesión exitoso", response.getMessage());
        assertNotNull(response.getToken());
        assertEquals("fake-jwt-token", response.getToken());
    }

    @Test
    public void testLoginUsuarioNoEncontrado() {
        // Datos de prueba
        String userName = "noExiste";

        // Mockear el comportamiento del repositorio
        when(usuarioRepository.findByUserName(userName)).thenReturn(Optional.empty());

        // Ejecutar el servicio de login y esperar una excepción
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setUserNameOrEmail(userName);
        loginRequest.setPassword("cualquierCosa");

        Exception exception = assertThrows(Exception.class, () -> {
            loginService.login(loginRequest);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    public void testLoginContraseñaIncorrecta() {
        // Datos de prueba
        String userName = "juanperez";
        String wrongPassword = "contraseñaIncorrecta";
        String correctPassword = BCrypt.hashpw("miPasswordSegura", BCrypt.gensalt());
        Usuario usuario = new Usuario();
        usuario.setUserName(userName);
        usuario.setPassword(correctPassword);

        // Mockear el comportamiento del repositorio
        when(usuarioRepository.findByUserName(userName)).thenReturn(Optional.of(usuario));

        // Ejecutar el servicio de login y esperar una excepción
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setUserNameOrEmail(userName);
        loginRequest.setPassword(wrongPassword);

        Exception exception = assertThrows(Exception.class, () -> {
            loginService.login(loginRequest);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Contraseña incorrecta", exception.getMessage());
    }

}