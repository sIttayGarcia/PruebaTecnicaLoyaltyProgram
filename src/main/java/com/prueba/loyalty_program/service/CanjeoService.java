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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanjeoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private CanjeRepository canjeRepository;


    public ResponseCanje canjearRecompensa(CanjeDTO canjeDTO)throws Exception {
        //Buscamos el usuario
        Usuario usuario = usuarioRepository.findByUserName(canjeDTO.getUsername())
                .orElseThrow(()->new UsuarioNotFoundException("Usuario no encontrado"));

        //Buscamos Recompensa
        Recompensa recompensa = recompensaRepository.findById(canjeDTO.getRecompensaId())
                .orElseThrow(()-> new Exception("Recompensa no encontrada"));

        //Verificamos si usuario tiene puntos suficientes
        if (usuario.getSaldoPuntos() < (recompensa.getPuntosNecesarios() * canjeDTO.getCantidad())){
            throw new Exception("Puntos insuficientes para canjear recompensa");
        }

        //Restamos los puntos al usuario
        usuario.setSaldoPuntos(usuario.getSaldoPuntos() - recompensa.getPuntosNecesarios());

        //Actualizamos el usuario con nuevo saldo
        usuarioRepository.save(usuario);

        //Guardamos la transaccion en la tabla de canjes como historial
        Canje nuevaTransaccion = new Canje();
        nuevaTransaccion.setUsuario(usuario);
        nuevaTransaccion.setRecompensa(recompensa);
        canjeRepository.save(nuevaTransaccion);


        ResponseCanje response = new ResponseCanje(usuario,recompensa);
        response.setMessage("Accion confirmada");

        return response;
    }

    public List<ResponseHistoriaCanjes> detalleCanjesUsuario(String userName)
            throws UsuarioNotFoundException, CanjeoException {
        //validamos si usuario existe
        Usuario usuario = usuarioRepository.findByUserName(userName)
                .orElseThrow(()-> new UsuarioNotFoundException("Usuario no encontrado"));
        //Obtenemos historial
        List<Canje> canjes = canjeRepository.findByUsuarioId(usuario.getId());

        // Si no tiene canjes, lanzamos una excepción
        if (canjes.isEmpty()) {
            throw new CanjeoException("El usuario no tiene canjes de recompensas");
        }


        // Convertir cada registro de canje a un DTO
        return canjes.stream()
                .map(canje -> new ResponseHistoriaCanjes(canje))
                .collect(Collectors.toList());

    }
}
