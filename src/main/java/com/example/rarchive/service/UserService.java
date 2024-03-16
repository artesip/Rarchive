package com.example.rarchive.service;


import com.example.rarchive.config.SecurityConfig;
import com.example.rarchive.entity.UserEntity;
import com.example.rarchive.model.UserModel;
import com.example.rarchive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public long getUserIdByLogin(String login) {
        return userRepository.findByLogin(login).get().getId();
    }


    public Optional<UserEntity> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void save(UserEntity user) {
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }


    public ResponseEntity<?> getUser() {
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<UserEntity> user = userRepository.findByLogin(login);

            return ResponseEntity.ok(user.map(UserModel::toModel).orElse(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> updateUser(UserEntity user) {
        try {

            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            if (user == null || login == null) {
                return ResponseEntity.badRequest().body("Empty user or login!\n");
            }

            UserEntity userCopy = userRepository.findByLogin(login).get();

            if (!user.equals(userCopy)) {
                userCopy.updateFields(user);
                save(userCopy);
            } else {
                return null;
            }

            return ResponseEntity.ok("User was successfully updated!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteUser() {
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();

            userRepository.deleteByLogin(login);

            if (userRepository.findByLogin(login).isEmpty()) {
                return ResponseEntity.ok("User was successfully deleted!\n");
            }

            return ResponseEntity.badRequest().body("Something went wrong!\n");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong!\n" + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found ", username)
        ));

        return new User(
                user.getLogin(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
