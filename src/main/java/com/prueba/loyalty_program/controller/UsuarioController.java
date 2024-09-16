package com.prueba.loyalty_program.controller;

import com.prueba.loyalty_program.dto.UsuarioDTO;
import com.prueba.loyalty_program.entity.AccionUsuario;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.exceptionHandler.UsuarioException;
import com.prueba.loyalty_program.model.ResponseDetailAccion;
import com.prueba.loyalty_program.model.ResponseUsuario;
import com.prueba.loyalty_program.model.ResponseUsuarioDetail;
import com.prueba.loyalty_program.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuarioDTO);
            ResponseUsuario responseUsuario = new ResponseUsuario(usuarioCreado);
            return new ResponseEntity<>(responseUsuario, HttpStatus.OK);
        } catch (UsuarioException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{usuarioId}/acciones/{accionId}/acumular")
    public ResponseEntity<?> acumularPuntos(@PathVariable Long usuarioId, @PathVariable Long accionId) {
        AccionUsuario nuevaAccionUsuario = usuarioService.acumularPuntos(usuarioId, accionId);
        ResponseDetailAccion detailAccion = new ResponseDetailAccion(nuevaAccionUsuario);

        return new ResponseEntity<>(detailAccion,HttpStatus.OK);
    }

    @GetMapping("/consultar/username/{userName}")
    public ResponseEntity<?> obtenerDetalleUsuario(@PathVariable String userName) {
        try {
            ResponseUsuarioDetail detalleUsuario = usuarioService.obtenerDetalle(userName);
            return new ResponseEntity<>(detalleUsuario,HttpStatus.OK);
        }catch (UsuarioException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/consultar/id/{id}")
    public ResponseEntity<?> obtenerDetalleUsuarioById(@PathVariable Long id) {
        try {
            ResponseUsuarioDetail detalleUsuario = usuarioService.obtenerDetalleById(id);
            return new ResponseEntity<>(detalleUsuario,HttpStatus.OK);
        }catch (UsuarioException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para obtener el detalle de todos los usuarios
    @GetMapping("/detalle/todos")
    public ResponseEntity<List<ResponseUsuarioDetail>> getAllUser() {
        List<ResponseUsuarioDetail> detallesUsuarios = usuarioService.getAllUsers();
        return new ResponseEntity<>(detallesUsuarios, HttpStatus.OK);
    }

    @GetMapping("/detalle/paginados")
    public ResponseEntity<Page<ResponseUsuarioDetail>> obtenerUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<ResponseUsuarioDetail> detallesUsuarios = usuarioService.obtenerUsuariosPaginados(page, size);
        return new ResponseEntity<>(detallesUsuarios, HttpStatus.OK);
    }
}
