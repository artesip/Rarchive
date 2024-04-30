package com.example.rarchive.controller;

import com.example.rarchive.entity.UserEntity;
import com.example.rarchive.model.AuthRequest;
import com.example.rarchive.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {
    private final AuthService authService;

    @Autowired
    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registrate(@RequestBody UserEntity user) {
        return authService.registrate(user);
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest) {
        return authService.auth(authRequest);
    }
}
