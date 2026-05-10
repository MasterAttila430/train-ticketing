package com.siemens.train.controller;

import com.siemens.train.entities.AppUserBE;
import com.siemens.train.repo.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository appUserRepository;

    public AuthController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        return appUserRepository.findByUsername(username)
                .filter(user -> user.getPasswordHash().equals(password))
                .map(user -> ResponseEntity.ok(Map.of(
                        "username", user.getUsername(),
                        "role", user.getRole().name()
                )))
                .orElse(ResponseEntity.status(401).build());
    }
}