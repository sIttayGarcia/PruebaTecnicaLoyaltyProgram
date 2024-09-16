package com.prueba.loyalty_program.controller;

import com.prueba.loyalty_program.dto.LoginDTO;
import com.prueba.loyalty_program.model.ResponseLogin;
import com.prueba.loyalty_program.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequestDTO) {
        try {
            ResponseLogin loginResponse = loginService.login(loginRequestDTO);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
