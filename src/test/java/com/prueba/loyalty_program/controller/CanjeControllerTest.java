package com.prueba.loyalty_program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.loyalty_program.dto.CanjeDTO;
import com.prueba.loyalty_program.entity.Canje;
import com.prueba.loyalty_program.entity.Recompensa;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.exceptionHandler.CanjeoException;
import com.prueba.loyalty_program.exceptionHandler.UsuarioNotFoundException;
import com.prueba.loyalty_program.model.ResponseCanje;
import com.prueba.loyalty_program.model.ResponseHistoriaCanjes;
import com.prueba.loyalty_program.service.CanjeoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CanjeControllerTest {
    @Mock
    private CanjeoService canjeoService; // Simulamos el servicio

    @InjectMocks
    private CanjeController canjeController; // Inyectamos el controlador a probar

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(canjeController).build();
    }

    // Prueba para canjear recompensa exitosamente
    @Test
    public void testCanjearRecompensaExitoso() throws Exception {
        // Datos de prueba
        CanjeDTO canjeDTO = new CanjeDTO();
        canjeDTO.setUsername("juanperez");
        canjeDTO.setRecompensaId(1L);

        ResponseCanje response = new ResponseCanje();
        response.setMessage("Canje exitoso");
        response.setSaldoRestante(100);

        // Simulamos el comportamiento del servicio
        when(canjeoService.canjearRecompensa(any(CanjeDTO.class))).thenReturn(response);

        // Ejecutamos la solicitud POST y verificamos la respuesta
        mockMvc.perform(post("/canjes/obtener/recompensa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(canjeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Canje exitoso"))
                .andExpect(jsonPath("$.saldoRestante").value(100));
    }

    // Prueba para manejar error en el canje de recompensa
    @Test
    public void testCanjearRecompensaError() throws Exception {
        // Datos de prueba
        CanjeDTO canjeDTO = new CanjeDTO();
        canjeDTO.setUsername("juanperez");
        canjeDTO.setRecompensaId(1L);

        // Simulamos que el servicio lanza una excepción
        when(canjeoService.canjearRecompensa(any(CanjeDTO.class))).thenThrow(new RuntimeException("Error en el canje"));

        // Ejecutamos la solicitud POST y verificamos la respuesta de error
        mockMvc.perform(post("/canjes/obtener/recompensa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(canjeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Error en el canje"));
    }

    // Prueba para obtener historial de canjes exitosamente
    @Test
    public void testObtenerHistorialCanjesExitoso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUserName("prueba");
        usuario.setSaldoPuntos(20);

        Recompensa recompensa = new Recompensa();
        recompensa.setNombre("prueba1");

        Canje canje = new Canje();
        canje.setId(1L);
        canje.setUsuario(usuario);
        canje.setRecompensa(recompensa);
        canje.setFechaCanje(LocalDateTime.of(2023, 9, 1, 0, 0));        // Datos de prueba

        Canje canje2 = new Canje();
        canje2.setId(2L);
        canje2.setUsuario(usuario);
        canje2.setRecompensa(recompensa);
        canje2.setFechaCanje(LocalDateTime.of(2023, 9, 5, 0, 0));


        List<ResponseHistoriaCanjes> historial = Arrays.asList(
                new ResponseHistoriaCanjes(canje),
                new ResponseHistoriaCanjes(canje)
        );

        // Simulamos el comportamiento del servicio
        when(canjeoService.detalleCanjesUsuario("juanperez")).thenReturn(historial);

        // Ejecutamos la solicitud GET y verificamos la respuesta
        mockMvc.perform(get("/canjes/historial/juanperez")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreRecompensa").value("prueba1"))
                .andExpect(jsonPath("$[1].nombreRecompensa").value("prueba1"));
    }

    // Prueba para manejar usuario no encontrado al obtener historial de canjes
    @Test
    public void testObtenerHistorialUsuarioNoEncontrado() throws Exception {
        // Simulamos que el servicio lanza una excepción de usuario no encontrado
        when(canjeoService.detalleCanjesUsuario("noExiste")).thenThrow(new UsuarioNotFoundException("Usuario no encontrado"));

        // Ejecutamos la solicitud GET y verificamos la respuesta de error
        mockMvc.perform(get("/canjes/historial/noExiste")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Usuario no encontrado"));
    }

    // Prueba para manejar error general al obtener historial de canjes
    @Test
    public void testObtenerHistorialError() throws Exception {
        // Simulamos que el servicio lanza una excepción genérica
        when(canjeoService.detalleCanjesUsuario("juanperez")).thenThrow(new CanjeoException("Error al obtener el historial"));

        // Ejecutamos la solicitud GET y verificamos la respuesta de error
        mockMvc.perform(get("/canjes/historial/juanperez")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Error al obtener el historial"));
    }

}