package com.example.login.controller;

import com.example.login.dto.ProfileRequest;
import com.example.login.dto.ProfileResponse;
import com.example.login.entity.User;
import com.example.login.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> registerUser(@Valid @RequestBody ProfileRequest user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(userService.createUser(user));
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
