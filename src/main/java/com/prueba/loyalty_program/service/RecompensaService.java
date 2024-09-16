package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.RecompensaDTO;
import com.prueba.loyalty_program.entity.Recompensa;
import com.prueba.loyalty_program.exceptionHandler.RecompensaException;
import com.prueba.loyalty_program.repository.RecompensaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Servicio para crear recompensas para los usuarios
@Service
public class RecompensaService {

    @Autowired
    private RecompensaRepository recompensaRepository;

    public Recompensa crearRecompensa(RecompensaDTO request) {
        //Verificamos si la recompensa ya existe
        if (recompensaRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new RecompensaException("Recompensa ya registrada");

        }

        //Creamos nueva recompensa
        Recompensa recompensa = new Recompensa();
        recompensa.setNombre(request.getNombre());
        recompensa.setDescripcion(request.getDescripcion());
        recompensa.setPuntosNecesarios(request.getPuntosNecesarios());

        return recompensaRepository.save(recompensa);


    }

}
