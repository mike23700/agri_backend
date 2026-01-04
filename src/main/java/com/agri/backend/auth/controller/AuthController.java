package com.agri.backend.auth.controller;

import com.agri.backend.auth.dto.LoginRequestDto;
import com.agri.backend.auth.dto.RegisterRequestDto;
import com.agri.backend.auth.service.IAuthService;
import com.agri.backend.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService; // Utilisation de l'interface (SOLID - Dependency Inversion)

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerDto) {
        try {
            return ResponseEntity.ok(authService.registerUser(registerDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginDto) {
        try {
            User user = authService.loginUser(loginDto);
            return ResponseEntity.ok(user); // Status 200 OK
        } catch (Exception e) {
            // Renvoie 401 (Non autorisé) avec le message d'erreur
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}