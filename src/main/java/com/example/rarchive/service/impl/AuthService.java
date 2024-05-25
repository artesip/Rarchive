package com.example.rarchive.service.impl;

import com.example.rarchive.entity.UserEntity;
import com.example.rarchive.model.AuthRequest;
import com.example.rarchive.model.Tokens;
import com.example.rarchive.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AuthService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<?> registrate(UserEntity user) {
        try {
            userService.save(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
        return ResponseEntity.ok("User was successful saved!\n");
    }

    public ResponseEntity<?> auth(AuthRequest request) {
        try {
            if (request == null || request.getLogin().isEmpty() && request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Empty request or login or password\n");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

            UserDetails userDetails = userService.loadUserByUsername(request.getLogin());

            return ResponseEntity.ok(new Tokens(jwtTokenUtil.generateRefreshToken(userDetails), jwtTokenUtil.generateAccessToken(userDetails)));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }
}
