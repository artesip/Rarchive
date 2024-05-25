package com.example.rarchive.service.impl;


import com.example.rarchive.config.SecurityConfig;
import com.example.rarchive.entity.UserEntity;
import com.example.rarchive.exception.NoSuchDataException;
import com.example.rarchive.exception.UnauthorizedAccessException;
import com.example.rarchive.model.UserModel;
import com.example.rarchive.repository.UserRepository;
import com.example.rarchive.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserServiceImpl(UserRepository userRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void save(UserEntity user) {
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public ResponseEntity<?> getUser() {
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<UserEntity> user = userRepository.findByLogin(login);

            return ResponseEntity.ok(user.map(UserModel::toModel).orElse(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateUser(UserEntity user) {
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            if (user == null || login == null) {
                return ResponseEntity.badRequest().body("Empty user or login!\n");
            }

            UserEntity userCopy = userRepository.findByLogin(login).orElseThrow(
                    NoSuchDataException::new
            );

            if (!userCopy.getLogin().equals(login)) {
                throw new UnauthorizedAccessException();
            }

            userCopy.updateFields(user);
            save(userCopy);

            return ResponseEntity.ok("User was successfully updated!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteUser() {
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            userRepository.deleteByLogin(login);

            if (userRepository.findByLogin(login).isPresent()) {
                return ResponseEntity.badRequest().body("Something went wrong!\n");
            }

            return ResponseEntity.ok("User was successfully deleted!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findUserByLogin(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found ", username)
        ));

        return new User(
                user.getLogin(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
