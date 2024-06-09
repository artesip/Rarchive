package com.example.rarchive.controller;

import com.example.rarchive.entity.UserEntity;
import com.example.rarchive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUser() {
        return userService.getUser();
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserEntity user) {
        return userService.updateUser(user);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        return userService.deleteUser();
    }
}
