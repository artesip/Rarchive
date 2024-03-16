package com.example.rarchive.controller;

import com.example.rarchive.entity.UserEntity;
import com.example.rarchive.model.AuthRequest;
import com.example.rarchive.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<?> registrate(@RequestBody UserEntity user) {
        return authService.registrate(user);
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest){
        return authService.auth(authRequest);
    }
}
