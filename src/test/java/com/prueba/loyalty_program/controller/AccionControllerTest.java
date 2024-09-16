package com.prueba.loyalty_program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.loyalty_program.dto.AccionDTO;
import com.prueba.loyalty_program.entity.Accion;
import com.prueba.loyalty_program.service.AccionService;
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
public class AccionControllerTest {

    @Mock
    private AccionService accionService; // Simulamos el servicio

    @InjectMocks
    private AccionController accionController; // Inyectamos el controlador que vamos a probar

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accionController).build();
    }

    @Test
    public void testCrearAccionExitoso() throws Exception {
        // Datos de prueba
        AccionDTO accionDTO = new AccionDTO();
        accionDTO.setNombre("Accion 1");
        accionDTO.setPuntos(5);

        Accion accion = new Accion();
        accion.setId(1L);
        accion.setNombre("Accion 1");
        accion.setPuntos(5);

        // Simulamos el comportamiento del servicio
        when(accionService.crearAccion(any(AccionDTO.class))).thenReturn(accion);

        // Ejecutamos la solicitud POST y verificamos la respuesta
        mockMvc.perform(post("/api/accion/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actionName").value("Accion 1"))
                .andExpect(jsonPath("$.message").value("Acción creada correctamente"));
    }

    @Test
    public void testCrearAccionError() throws Exception {
        // Datos de prueba
        AccionDTO accionDTO = new AccionDTO();
        accionDTO.setNombre("Accion 1");
        accionDTO.setPuntos(1);

        // Simulamos que el servicio lanza una excepción
        when(accionService.crearAccion(any(AccionDTO.class))).thenThrow(new RuntimeException("Error al crear la acción"));

        // Ejecutamos la solicitud POST y verificamos la respuesta de error
        mockMvc.perform(post("/api/accion/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Error al crear acción Error al crear la acción"));
    }

}