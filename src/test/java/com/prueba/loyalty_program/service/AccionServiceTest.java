package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.AccionDTO;
import com.prueba.loyalty_program.entity.Accion;
import com.prueba.loyalty_program.repository.AccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AccionServiceTest {

    @Mock
    private AccionRepository accionRepository;

    @InjectMocks
    private AccionService accionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para crear una acción exitosamente
    @Test
    public void testCrearAccionExitoso() throws Exception {
        AccionDTO accionDTO = new AccionDTO();
        accionDTO.setNombre("Comprar");
        accionDTO.setPuntos(100);

        Accion accion = new Accion();
        accion.setId(1L);
        accion.setNombre("Comprar");
        accion.setPuntos(100);

        // Simulamos que no existe ninguna acción con el mismo nombre
        when(accionRepository.findByNombre("Comprar")).thenReturn(Optional.empty());
        when(accionRepository.save(any(Accion.class))).thenReturn(accion);

        Accion resultado = accionService.crearAccion(accionDTO);

        assertNotNull(resultado);
        assertEquals("Comprar", resultado.getNombre());
        assertEquals(100, resultado.getPuntos());
        verify(accionRepository, times(1)).save(any(Accion.class));
    }

    // Prueba para manejo de excepción al intentar crear una acción ya registrada
    @Test
    public void testCrearAccionYaRegistrada() {
        AccionDTO accionDTO = new AccionDTO();
        accionDTO.setNombre("Comprar");
        accionDTO.setPuntos(100);

        Accion accionExistente = new Accion();
        accionExistente.setId(1L);
        accionExistente.setNombre("Comprar");

        // Simulamos que ya existe una acción con el mismo nombre
        when(accionRepository.findByNombre("Comprar")).thenReturn(Optional.of(accionExistente));

        // Verificamos que se lanza la excepción
        Exception exception = assertThrows(Exception.class, () -> {
            accionService.crearAccion(accionDTO);
        });

        assertEquals("La accion ya está resgistrada", exception.getMessage());

        // Verificamos que no se llamó al método save
        verify(accionRepository, times(0)).save(any(Accion.class));
    }
}