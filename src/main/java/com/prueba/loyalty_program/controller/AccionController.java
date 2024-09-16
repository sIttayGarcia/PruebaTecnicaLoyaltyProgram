package com.prueba.loyalty_program.controller;

import com.prueba.loyalty_program.dto.AccionDTO;
import com.prueba.loyalty_program.entity.Accion;
import com.prueba.loyalty_program.model.ResponseAccion;
import com.prueba.loyalty_program.service.AccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccionController {

    @Autowired
    private AccionService accionService;

    @PostMapping("/accion/crear")
    public ResponseEntity<?> crearAccion(@RequestBody AccionDTO accionDTO) {
        try {
            Accion nuevaAccion = accionService.crearAccion(accionDTO);
            ResponseAccion responseAccion = new ResponseAccion(nuevaAccion);
            return new ResponseEntity<>(responseAccion,HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear acción "+ e.getMessage());
        }
    }
}
