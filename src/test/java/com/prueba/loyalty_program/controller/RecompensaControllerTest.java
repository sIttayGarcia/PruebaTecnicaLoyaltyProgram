package com.prueba.loyalty_program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.loyalty_program.dto.RecompensaDTO;
import com.prueba.loyalty_program.entity.Recompensa;
import com.prueba.loyalty_program.exceptionHandler.RecompensaException;
import com.prueba.loyalty_program.service.RecompensaService;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class RecompensaControllerTest {
    @Mock
    private RecompensaService recompensaService;

    @InjectMocks
    private RecompensaController recompensaController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(recompensaController).build();
    }

    // Prueba para crear una recompensa exitosamente
    @Test
    public void testCrearRecompensaExitoso() throws Exception {
        RecompensaDTO recompensaDTO = new RecompensaDTO();
        recompensaDTO.setNombre("Recompensa Premium");
        recompensaDTO.setPuntosNecesarios(100);

        Recompensa recompensa = new Recompensa();
        recompensa.setId(1L);
        recompensa.setNombre("Recompensa Premium");
        recompensa.setPuntosNecesarios(100);

        when(recompensaService.crearRecompensa(any(RecompensaDTO.class))).thenReturn(recompensa);

        mockMvc.perform(post("/recompensa/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recompensaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Recompensa Premium"));
    }

    // Prueba para manejo de error al crear una recompensa
    @Test
    public void testCrearRecompensaError() throws Exception {
        RecompensaDTO recompensaDTO = new RecompensaDTO();
        recompensaDTO.setNombre("Recompensa Premium");
        recompensaDTO.setPuntosNecesarios(100);

        when(recompensaService.crearRecompensa(any(RecompensaDTO.class))).thenThrow(new RecompensaException("Error al crear la recompensa"));

        mockMvc.perform(post("/recompensa/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(recompensaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Error al crear la recompensa"));
    }

}