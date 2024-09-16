package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.UsuarioDTO;
import com.prueba.loyalty_program.entity.Accion;
import com.prueba.loyalty_program.entity.AccionUsuario;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.exceptionHandler.UsuarioException;
import com.prueba.loyalty_program.exceptionHandler.UsuarioNotFoundException;
import com.prueba.loyalty_program.model.ResponseUsuarioDetail;
import com.prueba.loyalty_program.repository.AccionRepository;
import com.prueba.loyalty_program.repository.AccionUsuarioRepository;
import com.prueba.loyalty_program.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AccionRepository accionRepository;

    @Mock
    private AccionUsuarioRepository accionUsuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Prueba para crear usuario exitosamente
    @Test
    public void testCrearUsuarioExitoso() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setUserName("juanperez");
        usuarioDTO.setEmail("juan@example.com");
        usuarioDTO.setPassword("password123");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setUserName("juanperez");
        usuario.setEmail("juan@example.com");

        // Simulamos que el correo y el username no existen
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.crearUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juanperez", resultado.getUserName());
        assertEquals("juan@example.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // Prueba para manejo de excepción al crear usuario con email existente
    @Test
    public void testCrearUsuarioEmailExistente() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setUserName("juanperez");
        usuarioDTO.setEmail("juan@example.com");

        when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(new Usuario()));

        UsuarioException exception = assertThrows(UsuarioException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });

        assertEquals("El email ya está registrado", exception.getMessage());
    }

    // Prueba para manejo de excepción al crear usuario con username existente
    @Test
    public void testCrearUsuarioUsernameExistente() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setUserName("juanperez");
        usuarioDTO.setEmail("juan@example.com");

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.of(new Usuario()));

        UsuarioException exception = assertThrows(UsuarioException.class, () -> {
            usuarioService.crearUsuario(usuarioDTO);
        });

        assertEquals("El username ya está registrado", exception.getMessage());
    }

    // Prueba para acumular puntos exitosamente
    @Test
    public void testAcumularPuntosExitoso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSaldoPuntos(50);

        Accion accion = new Accion();
        accion.setId(1L);
        accion.setPuntos(10);

        AccionUsuario accionUsuario = new AccionUsuario();
        accionUsuario.setId(1L);
        accionUsuario.setUsuario(usuario);
        accionUsuario.setAccion(accion);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(accionRepository.findById(1L)).thenReturn(Optional.of(accion));
        when(accionUsuarioRepository.save(any(AccionUsuario.class))).thenReturn(accionUsuario);

        AccionUsuario resultado = usuarioService.acumularPuntos(1L, 1L);

        assertNotNull(resultado);
        assertEquals(60, usuario.getSaldoPuntos()); // 50 + 10
        verify(usuarioRepository, times(1)).save(usuario);
    }

    // Prueba para manejo de excepción cuando usuario no es encontrado
    @Test
    public void testAcumularPuntosUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        UsuarioNotFoundException exception = assertThrows(UsuarioNotFoundException.class, () -> {
            usuarioService.acumularPuntos(1L, 1L);
        });

        assertEquals("Usuario con id 1 no encontrado", exception.getMessage());
    }

    // Prueba para obtener detalle de usuario por username exitosamente
    @Test
    public void testObtenerDetalleUsuarioExitoso() {
        Usuario usuario = new Usuario();
        usuario.setUserName("juanperez");
        usuario.setSaldoPuntos(100);

        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.of(usuario));

        ResponseUsuarioDetail resultado = usuarioService.obtenerDetalle("juanperez");

        assertNotNull(resultado);
        assertEquals("juanperez", resultado.getUserName());
        assertEquals(100, resultado.getSaldoActual());
    }

    // Prueba para obtener detalle de usuario por username no encontrado
    @Test
    public void testObtenerDetalleUsuarioNoEncontrado() {
        when(usuarioRepository.findByUserName("juanperez")).thenReturn(Optional.empty());

        UsuarioNotFoundException exception = assertThrows(UsuarioNotFoundException.class, () -> {
            usuarioService.obtenerDetalle("juanperez");
        });

        assertEquals("Usuario con username juanperez no encontrado", exception.getMessage());
    }

    @Test
    public void testObtenerUsuariosPaginados() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan");
        usuario1.setUserName("juanperez");
        usuario1.setEmail("juan@example.com");
        usuario1.setSaldoPuntos(100);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Ana");
        usuario2.setUserName("anagarcia");
        usuario2.setEmail("ana@example.com");
        usuario2.setSaldoPuntos(200);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        Page<Usuario> pageUsuarios = new PageImpl<>(usuarios);

        when(usuarioRepository.findAll(any(PageRequest.class))).thenReturn(pageUsuarios);

        Page<ResponseUsuarioDetail> resultado = usuarioService.obtenerUsuariosPaginados(0, 5);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        assertEquals("juanperez", resultado.getContent().get(0).getUserName());
        assertEquals("anagarcia", resultado.getContent().get(1).getUserName());
    }

    @Test
    public void testObtenerDetalleByIdExitoso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setUserName("juanperez");
        usuario.setSaldoPuntos(100);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ResponseUsuarioDetail resultado = usuarioService.obtenerDetalleById(1L);

        assertNotNull(resultado);
        assertEquals("juanperez", resultado.getUserName());
        assertEquals(100, resultado.getSaldoActual());
    }

    // Prueba para manejo de excepción cuando el usuario no es encontrado
    @Test
    public void testObtenerDetalleByIdUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        UsuarioNotFoundException exception = assertThrows(UsuarioNotFoundException.class, () -> {
            usuarioService.obtenerDetalleById(1L);
        });

        assertEquals("Usuario con username 1 no encontrado", exception.getMessage());
    }

    // Prueba para usuario sin puntos acumulados
    @Test
    public void testObtenerDetalleByIdUsuarioSinPuntos() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setUserName("juanperez");
        usuario.setSaldoPuntos(0);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ResponseUsuarioDetail resultado = usuarioService.obtenerDetalleById(1L);

        assertNotNull(resultado);
        assertEquals("juanperez", resultado.getUserName());
        assertEquals(0, resultado.getSaldoActual());
        assertEquals("Usuario Juan no tiene puntos acumulados", resultado.getMessage());  // Verificamos el mensaje
    }

    @Test
    public void testGetAllUsersExitoso() {

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Juan");
        usuario1.setUserName("juanperez");
        usuario1.setEmail("juan@example.com");
        usuario1.setSaldoPuntos(100);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Ana");
        usuario2.setUserName("anagarcia");
        usuario2.setEmail("ana@example.com");
        usuario2.setSaldoPuntos(200);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<ResponseUsuarioDetail> resultado = usuarioService.getAllUsers();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("juanperez", resultado.get(0).getUserName());
        assertEquals("anagarcia", resultado.get(1).getUserName());
    }

    // Prueba para caso en que no hay usuarios
    @Test
    public void testGetAllUsersListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<ResponseUsuarioDetail> resultado = usuarioService.getAllUsers();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());  // Verificamos que la lista esté vacía
    }
}