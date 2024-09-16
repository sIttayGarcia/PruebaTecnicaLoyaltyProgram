package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.UsuarioDTO;
import com.prueba.loyalty_program.entity.Accion;
import com.prueba.loyalty_program.entity.AccionUsuario;
import com.prueba.loyalty_program.entity.Usuario;
import com.prueba.loyalty_program.exceptionHandler.AccionNotFoundException;
import com.prueba.loyalty_program.exceptionHandler.UsuarioException;
import com.prueba.loyalty_program.exceptionHandler.UsuarioNotFoundException;
import com.prueba.loyalty_program.model.ResponseUsuarioDetail;
import com.prueba.loyalty_program.repository.AccionRepository;
import com.prueba.loyalty_program.repository.AccionUsuarioRepository;
import com.prueba.loyalty_program.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AccionRepository accionRepository;

    @Autowired
    private AccionUsuarioRepository accionUsuarioRepository;

    public Usuario crearUsuario(UsuarioDTO usuarioDTO) throws UsuarioException {
        //Verificamos si el correo generado ya existe
        // Verificamos si el correo o username ya existen
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new UsuarioException("El email ya está registrado");
        }

        if (usuarioRepository.findByUserName(usuarioDTO.getUserName()).isPresent()) {
            throw new UsuarioException("El username ya está registrado");
        }
        //creamos nuevo usuario
        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setNombre(usuarioDTO.getNombre());
        usuarioNuevo.setUserName(usuarioDTO.getUserName());
        usuarioNuevo.setEmail(usuarioDTO.getEmail());

        // Hash de la contraseña antes de guardarla
        String contrasenaHash = BCrypt.hashpw(usuarioDTO.getPassword(), BCrypt.gensalt());
        usuarioNuevo.setPassword(contrasenaHash);

        //Guardamos el usuario
        return usuarioRepository.save(usuarioNuevo);

    }

    public AccionUsuario acumularPuntos(Long usuarioId, Long accionId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNotFoundException("Usuario con id " + usuarioId + " no encontrado"));


        Accion accion = accionRepository.findById(accionId).orElseThrow(() -> new AccionNotFoundException("Acción con id " + accionId + " no encontrado"));

        //Actualizamos los puntos al hacer una compra del usuario

        usuario.setSaldoPuntos(usuario.getSaldoPuntos() + accion.getPuntos());
        usuarioRepository.save(usuario);

        //registramos la accion del usuario
        AccionUsuario nuevaTransaccion = new AccionUsuario();
        nuevaTransaccion.setUsuario(usuario);
        nuevaTransaccion.setAccion(accion);
        return accionUsuarioRepository.save(nuevaTransaccion);
    }

    public ResponseUsuarioDetail obtenerDetalle(String userName) {
        Usuario usuario = usuarioRepository.findByUserName(userName).orElseThrow(() -> new UsuarioNotFoundException("Usuario con username " + userName + " no encontrado"));

        ResponseUsuarioDetail response = new ResponseUsuarioDetail(usuario);

        //validacion
        if (usuario.getSaldoPuntos() == 0) {
            response.setMessage("Usuario " + usuario.getNombre() + " no tiene puntos acumulados");
        }
        return response;

    }

    public ResponseUsuarioDetail obtenerDetalleById(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException("Usuario con username " + id + " no encontrado"));

        ResponseUsuarioDetail response = new ResponseUsuarioDetail(usuario);

        //validacion
        if (usuario.getSaldoPuntos() == 0) {
            response.setMessage("Usuario " + usuario.getNombre() + " no tiene puntos acumulados");
        }
        return response;

    }

    public List<ResponseUsuarioDetail> getAllUsers() {
        //Obtener todos los usuarios de la base de datos
        List<Usuario> usuarios = usuarioRepository.findAll();

        //Convertimos cada usuario en objeto Response
        return usuarios.stream().map(ResponseUsuarioDetail::new).collect(Collectors.toList());
    }

    public Page<ResponseUsuarioDetail> obtenerUsuariosPaginados(int page, int size) {
        Page<Usuario> usuarios = usuarioRepository.findAll(PageRequest.of(page, size));

        //Convertimos cada usuario en objeto Response
        return usuarios.map(ResponseUsuarioDetail::new);
    }
}
