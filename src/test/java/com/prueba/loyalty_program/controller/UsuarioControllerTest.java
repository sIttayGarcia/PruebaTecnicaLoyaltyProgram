package com.prueba.loyalty_program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.loyalty_program.dto.UsuarioDTO;
import com.prueba.loyalty_program.entity.Accion;
import com.prueba.loyalty_program.entity.AccionUsuario;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.exceptionHandler.UsuarioException;
import com.prueba.loyalty_program.model.ResponseUsuarioDetail;
import com.prueba.loyalty_program.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {


    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    // Test para crear usuario exitosamente
    @Test
    public void testCrearUsuarioExitoso() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setUserName("juanperez");
        usuarioDTO.setEmail("juan@example.com");
        usuarioDTO.setPassword("password123");

        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setId(1L);
        usuarioCreado.setNombre("Juan");
        usuarioCreado.setUserName("juanperez");
        usuarioCreado.setEmail("juan@example.com");

        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuarioCreado);

        mockMvc.perform(post("/usuarios/crear").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(usuarioDTO))).andExpect(status().isOk()).andExpect(jsonPath("$.userName").value("juanperez"));
    }

    // Test para manejo de error al crear usuario
    @Test
    public void testCrearUsuarioError() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setUserName("juanperez");
        usuarioDTO.setEmail("juan@example.com");
        usuarioDTO.setPassword("password123");

        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenThrow(new UsuarioException("Error al crear usuario"));

        mockMvc.perform(post("/usuarios/crear").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(usuarioDTO))).andExpect(status().isBadRequest()).andExpect(jsonPath("$").value("Error al crear usuario"));
    }

    // Test para acumular puntos
    @Test
    public void testAcumularPuntosExitoso() throws Exception {
        // Crear usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUserName("juanperez");

        // Crear acción de prueba
        Accion accion = new Accion();
        accion.setId(1L);
        accion.setNombre("Descripción de la acción");

        // Crear AccionUsuario de prueba
        AccionUsuario accionUsuario = new AccionUsuario();
        accionUsuario.setId(1L);
        accionUsuario.setUsuario(usuario);
        accionUsuario.setAccion(accion);  // Asegúrate de que la acción no sea nula

        // Simular el servicio
        when(usuarioService.acumularPuntos(anyLong(), anyLong())).thenReturn(accionUsuario);

        // Ejecutar la solicitud PUT
        mockMvc.perform(put("/usuarios/1/acciones/2/acumular").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
    }

    // Test para obtener el detalle de un usuario por userName
    @Test
    public void testObtenerDetalleUsuarioExitoso() throws Exception {
        Usuario usuario = new Usuario(1L, "juanperez", "juan", "juan@example.com", 150);

        ResponseUsuarioDetail usuarioDetail = new ResponseUsuarioDetail(usuario);
        usuarioDetail.setUserName("juanperez");
        usuarioDetail.setSaldoActual(150);

        when(usuarioService.obtenerDetalle("juanperez")).thenReturn(usuarioDetail);

        mockMvc.perform(get("/usuarios/consultar/username/juanperez").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("juanperez")).andExpect(jsonPath("$.saldoActual").value(150));
    }

    // Test para obtener el detalle de un usuario por id
    @Test
    public void testObtenerDetalleUsuarioByIdExitoso() throws Exception {
        Usuario usuario = new Usuario(1L, "juanperez", "juan", "juan@example.com", 150);

        ResponseUsuarioDetail usuarioDetail = new ResponseUsuarioDetail(usuario);
        usuarioDetail.setUserName("juanperez");
        usuarioDetail.setSaldoActual(150);

        when(usuarioService.obtenerDetalleById(1L)).thenReturn(usuarioDetail);

        mockMvc.perform(get("/usuarios/consultar/id/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.userName").value("juanperez")).andExpect(jsonPath("$.saldoActual").value(150));
    }

    // Test para obtener todos los usuarios
    @Test
    public void testGetAllUserExitoso() throws Exception {

        Usuario usuario = new Usuario(1L, "anagarcia", "anagarcia", "juan@example.com", 150);
        Usuario usuario2 = new Usuario(1L, "anagarcia", "anagarcia", "juan@example.com", 150);

        List<ResponseUsuarioDetail> usuarios = Arrays.asList(new ResponseUsuarioDetail(usuario), new ResponseUsuarioDetail(usuario2));

        when(usuarioService.getAllUsers()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios/detalle/todos").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$[0].saldoActual").value(0)).andExpect(jsonPath("$[1].saldoActual").value(0));
    }

    // Test para obtener usuarios paginados
    @Test
    public void testObtenerUsuariosPaginados() throws Exception {
        List<ResponseUsuarioDetail> usuarios = Arrays.asList(new ResponseUsuarioDetail(new Usuario(1L, "Juan", "juanperez", "juan@example.com", 150)), new ResponseUsuarioDetail(new Usuario(2L, "Ana", "anagarcia", "ana@example.com", 200)));

        Page<ResponseUsuarioDetail> page = new PageImpl<>(usuarios);

        when(usuarioService.obtenerUsuariosPaginados(0, 5)).thenReturn(page);

        mockMvc.perform(get("/usuarios/detalle/paginados").param("page", "0").param("size", "5").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].saldoActual").value(0)).andExpect(jsonPath("$.content[1].saldoActual").value(0));
    }

    @Test
    public void testObtenerDetalleUsuarioError() throws Exception {
        // Simulamos que el servicio lanza una excepción UsuarioException
        when(usuarioService.obtenerDetalle("juanperez")).thenThrow(new UsuarioException("Usuario no encontrado"));

        // Verificamos que se maneje la excepción y se devuelva el mensaje de error con el código HTTP 400
        mockMvc.perform(get("/usuarios/consultar/username/juanperez")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Usuario no encontrado"));
    }

    // Prueba para manejar error (excepción) al obtener el detalle de un usuario por id
    @Test
    public void testObtenerDetalleUsuarioByIdError() throws Exception {
        // Simulamos que el servicio lanza una excepción UsuarioException
        when(usuarioService.obtenerDetalleById(1L)).thenThrow(new UsuarioException("Usuario no encontrado"));

        // Verificamos que se maneje la excepción y se devuelva el mensaje de error con el código HTTP 400
        mockMvc.perform(get("/usuarios/consultar/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Usuario no encontrado"));
    }

}