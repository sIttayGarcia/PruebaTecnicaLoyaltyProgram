package com.prueba.loyalty_program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.loyalty_program.dto.LoginDTO;
import com.prueba.loyalty_program.model.ResponseLogin;
import com.prueba.loyalty_program.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void testLoginExitoso() throws Exception {
        // Datos de prueba
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setUserNameOrEmail("juanperez");
        loginRequest.setPassword("miPasswordSegura");

        // Mockear el servicio
        when(loginService.login(any(LoginDTO.class))).thenReturn(new ResponseLogin("Inicio de sesión exitoso", "fake-jwt-token"));

        // Ejecutar la solicitud POST
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inicio de sesión exitoso"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    public void testLoginUsuarioNoEncontrado() throws Exception {
        // Datos de prueba
        LoginDTO loginRequest = new LoginDTO();
        loginRequest.setUserNameOrEmail("noExiste");
        loginRequest.setPassword("password");

        // Mockear el servicio para lanzar una excepción
        when(loginService.login(any(LoginDTO.class))).thenThrow(new Exception("Usuario no encontrado"));

        // Ejecutar la solicitud POST
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Usuario no encontrado"));
    }
}