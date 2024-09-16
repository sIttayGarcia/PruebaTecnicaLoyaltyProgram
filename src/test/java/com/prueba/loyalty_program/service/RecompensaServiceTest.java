package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.RecompensaDTO;
import com.prueba.loyalty_program.entity.Recompensa;
import com.prueba.loyalty_program.exceptionHandler.RecompensaException;
import com.prueba.loyalty_program.repository.RecompensaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecompensaServiceTest {
    @Mock
    private RecompensaRepository recompensaRepository;

    @InjectMocks
    private RecompensaService recompensaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para crear recompensa exitosamente
    @Test
    public void testCrearRecompensaExitoso() {
        RecompensaDTO recompensaDTO = new RecompensaDTO();
        recompensaDTO.setNombre("Recompensa Premium");
        recompensaDTO.setDescripcion("Descripción de la recompensa");
        recompensaDTO.setPuntosNecesarios(100);

        Recompensa recompensa = new Recompensa();
        recompensa.setId(1L);
        recompensa.setNombre("Recompensa Premium");
        recompensa.setDescripcion("Descripción de la recompensa");
        recompensa.setPuntosNecesarios(100);

        // Simulamos que no existe ninguna recompensa con el mismo nombre
        when(recompensaRepository.findByNombre("Recompensa Premium")).thenReturn(Optional.empty());
        when(recompensaRepository.save(any(Recompensa.class))).thenReturn(recompensa);

        Recompensa resultado = recompensaService.crearRecompensa(recompensaDTO);

        // Verificamos que el resultado sea el esperado
        assertNotNull(resultado);
        assertEquals("Recompensa Premium", resultado.getNombre());
        assertEquals("Descripción de la recompensa", resultado.getDescripcion());
        assertEquals(100, resultado.getPuntosNecesarios());

        // Verificamos que se llamó al método save
        verify(recompensaRepository, times(1)).save(any(Recompensa.class));
    }

    // Prueba para manejo de excepción al intentar crear una recompensa ya existente
    @Test
    public void testCrearRecompensaYaExiste() {
        RecompensaDTO recompensaDTO = new RecompensaDTO();
        recompensaDTO.setNombre("Recompensa Premium");
        recompensaDTO.setDescripcion("Descripción de la recompensa");
        recompensaDTO.setPuntosNecesarios(100);

        Recompensa recompensaExistente = new Recompensa();
        recompensaExistente.setId(1L);
        recompensaExistente.setNombre("Recompensa Premium");

        // Simulamos que ya existe una recompensa con el mismo nombre
        when(recompensaRepository.findByNombre("Recompensa Premium")).thenReturn(Optional.of(recompensaExistente));

        // Verificamos que se lanza la excepción
        RecompensaException exception = assertThrows(RecompensaException.class, () -> {
            recompensaService.crearRecompensa(recompensaDTO);
        });

        // Verificamos que el mensaje de la excepción es el esperado
        assertEquals("Recompensa ya registrada", exception.getMessage());

        // Verificamos que no se llamó al método save
        verify(recompensaRepository, times(0)).save(any(Recompensa.class));
    }

}