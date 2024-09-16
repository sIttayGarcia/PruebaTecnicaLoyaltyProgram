package com.prueba.loyalty_program.controller;

import com.prueba.loyalty_program.dto.CanjeDTO;
import com.prueba.loyalty_program.exceptionHandler.CanjeoException;
import com.prueba.loyalty_program.exceptionHandler.UsuarioNotFoundException;
import com.prueba.loyalty_program.model.ResponseCanje;
import com.prueba.loyalty_program.model.ResponseHistoriaCanjes;
import com.prueba.loyalty_program.service.CanjeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canjes")
public class CanjeController {

    @Autowired
    private CanjeoService service;

    @PostMapping("/obtener/recompensa")
    public ResponseEntity<?> canjearRecompensa(@RequestBody CanjeDTO canjeDTO){
        try{
            ResponseCanje response = service.canjearRecompensa(canjeDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/historial/{userName}")
    public ResponseEntity<?> obtenerHistorialCanjesPorUsuario(@PathVariable String userName) {
        try {
            List<ResponseHistoriaCanjes> historial = service.detalleCanjesUsuario(userName);
            return new ResponseEntity<>(historial, HttpStatus.OK);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (CanjeoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
