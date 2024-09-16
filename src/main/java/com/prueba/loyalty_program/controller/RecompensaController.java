package com.prueba.loyalty_program.controller;

import com.prueba.loyalty_program.dto.RecompensaDTO;
import com.prueba.loyalty_program.entity.Recompensa;
import com.prueba.loyalty_program.exceptionHandler.RecompensaException;
import com.prueba.loyalty_program.model.ResponseRecompensa;
import com.prueba.loyalty_program.service.RecompensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recompensa")
public class RecompensaController {

    @Autowired
    private RecompensaService recompensaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearRecompensa(@RequestBody RecompensaDTO recompensaDTO) {
        try {

            Recompensa recompensa = recompensaService.crearRecompensa(recompensaDTO);
            ResponseRecompensa responseRecompensa = new ResponseRecompensa(recompensa);
            return new ResponseEntity<>(responseRecompensa, HttpStatus.OK);
        }catch (RecompensaException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
