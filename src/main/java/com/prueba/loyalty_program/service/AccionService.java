package com.prueba.loyalty_program.service;

import com.prueba.loyalty_program.dto.AccionDTO;
import com.prueba.loyalty_program.entity.Accion;
import com.prueba.loyalty_program.repository.AccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccionService {

    @Autowired
    private AccionRepository accionRepository;

    public Accion crearAccion(AccionDTO accionDTO) throws Exception {
        if (accionRepository.findByNombre(accionDTO.getNombre()).isPresent()) {
            throw new Exception("La accion ya está resgistrada");
        }

        //Creamos la accion recompensa
        Accion nuevaAccion = new Accion();
        nuevaAccion.setNombre(accionDTO.getNombre());
        nuevaAccion.setPuntos(accionDTO.getPuntos());

        //Guardamos la accion
        return accionRepository.save(nuevaAccion);

    }
}
