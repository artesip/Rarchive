package com.example.rarchive.service;

import com.example.rarchive.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {

    Optional<UserEntity> findUserByLogin(String login);

    ResponseEntity<?> getUser();

    ResponseEntity<String> updateUser(UserEntity user);

    ResponseEntity<String> deleteUser();
}
