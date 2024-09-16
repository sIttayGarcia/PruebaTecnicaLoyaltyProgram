package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.CanjeDTO;
import com.prueba.loyalty_program.entity.Canje;
import com.prueba.loyalty_program.entity.Recompensa;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.exceptionHandler.CanjeoException;
import com.prueba.loyalty_program.exceptionHandler.UsuarioNotFoundException;
import com.prueba.loyalty_program.model.ResponseCanje;
import com.prueba.loyalty_program.model.ResponseHistoriaCanjes;
import com.prueba.loyalty_program.repository.CanjeRepository;
import com.prueba.loyalty_program.repository.RecompensaRepository;
import com.prueba.loyalty_program.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CanjeoServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RecompensaRepository recompensaRepository;

    @Mock
    private CanjeRepository canjeRepository;

    @InjectMocks
    private CanjeoService canjeoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para canjear recompensa exitosamente
    @Test
    public void testCanjearRecompensaExitoso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUserName("juanperez");
        usuario.setSaldoPuntos(500);

        Recompensa recompensa = new Recompensa();
        recompensa.setId(1L);
        recompensa.setPuntosNecesarios(100);

        CanjeDTO canjeDTO = new CanjeDTO();
        canjeDTO.setUsername("juanperez");
        canjeDTO.setRecompensaId(1L);
        canjeDTO.setCantidad(1);

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.of(usuario));
        when(recompensaRepository.findById(1L)).thenReturn(Optional.of(recompensa));

        ResponseCanje resultado = canjeoService.canjearRecompensa(canjeDTO);

        assertNotNull(resultado);
        assertEquals("Accion confirmada", resultado.getMessage());
        assertEquals(400, usuario.getSaldoPuntos());  // 500 - 100
        verify(canjeRepository, times(1)).save(any(Canje.class));
    }

    // Prueba para manejo de excepción de usuario no encontrado en canjear recompensa
    @Test
    public void testCanjearRecompensaUsuarioNoEncontrado() {
        CanjeDTO canjeDTO = new CanjeDTO();
        canjeDTO.setUsername("juanperez");
        canjeDTO.setRecompensaId(1L);
        canjeDTO.setCantidad(1);

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.empty());

        UsuarioNotFoundException exception = assertThrows(UsuarioNotFoundException.class, () -> {
            canjeoService.canjearRecompensa(canjeDTO);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    // Prueba para manejo de excepción de recompensa no encontrada en canjear recompensa
    @Test
    public void testCanjearRecompensaRecompensaNoEncontrada() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUserName("juanperez");
        usuario.setSaldoPuntos(500);

        CanjeDTO canjeDTO = new CanjeDTO();
        canjeDTO.setUsername("juanperez");
        canjeDTO.setRecompensaId(1L);
        canjeDTO.setCantidad(1);

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.of(usuario));
        when(recompensaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            canjeoService.canjearRecompensa(canjeDTO);
        });

        assertEquals("Recompensa no encontrada", exception.getMessage());
    }

    // Prueba para manejo de excepción de puntos insuficientes en canjear recompensa
    @Test
    public void testCanjearRecompensaPuntosInsuficientes() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUserName("juanperez");
        usuario.setSaldoPuntos(50);  // Pocos puntos

        Recompensa recompensa = new Recompensa();
        recompensa.setId(1L);
        recompensa.setPuntosNecesarios(100);  // Requiere más puntos

        CanjeDTO canjeDTO = new CanjeDTO();
        canjeDTO.setUsername("juanperez");
        canjeDTO.setRecompensaId(1L);
        canjeDTO.setCantidad(1);

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.of(usuario));
        when(recompensaRepository.findById(1L)).thenReturn(Optional.of(recompensa));

        Exception exception = assertThrows(Exception.class, () -> {
            canjeoService.canjearRecompensa(canjeDTO);
        });

        assertEquals("Puntos insuficientes para canjear recompensa", exception.getMessage());
    }

    // Prueba para obtener el detalle de canjes de usuario exitosamente
    @Test
    public void testDetalleCanjesUsuarioExitoso() throws UsuarioNotFoundException, CanjeoException {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUserName("juanperez");

        Recompensa recompensa1 = new Recompensa();
        recompensa1.setId(1L);
        recompensa1.setNombre("Recompensa 1");

        Recompensa recompensa2 = new Recompensa();
        recompensa2.setId(2L);
        recompensa2.setNombre("Recompensa 2");

        Canje canje1 = new Canje();
        canje1.setId(1L);
        canje1.setRecompensa(recompensa1);  // Asegúrate de asignar la recompensa

        Canje canje2 = new Canje();
        canje2.setId(2L);
        canje2.setRecompensa(recompensa2);  // Asegúrate de asignar la recompensa

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.of(usuario));
        when(canjeRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(canje1, canje2));

        List<ResponseHistoriaCanjes> resultado = canjeoService.detalleCanjesUsuario("juanperez");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Recompensa 1", resultado.get(0).getNombreRecompensa());
        assertEquals("Recompensa 2", resultado.get(1).getNombreRecompensa());
    }

    // Prueba para manejo de excepción de usuario no encontrado en detalle de canjes
    @Test
    public void testDetalleCanjesUsuarioNoEncontrado() {
        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.empty());

        UsuarioNotFoundException exception = assertThrows(UsuarioNotFoundException.class, () -> {
            canjeoService.detalleCanjesUsuario("juanperez");
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    // Prueba para manejo de excepción de historial vacío (sin canjes)
    @Test
    public void testDetalleCanjesUsuarioSinHistorial() throws UsuarioNotFoundException {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUserName("juanperez");

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.of(usuario));
        when(canjeRepository.findByUsuarioId(1L)).thenReturn(Collections.emptyList());

        CanjeoException exception = assertThrows(CanjeoException.class, () -> {
            canjeoService.detalleCanjesUsuario("juanperez");
        });

        assertEquals("El usuario no tiene canjes de recompensas", exception.getMessage());
    }

}