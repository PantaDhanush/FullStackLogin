package com.example.login.controller;

import com.example.login.dto.AuthRequest;
import com.example.login.dto.AuthResponse;
import com.example.login.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response=authService.login(authRequest.getEmail(), authRequest.getPassword());
        return ResponseEntity.ok()
                .header("Set-Cookie",response.getCookie().toString())
                .body(response);
    }
}
