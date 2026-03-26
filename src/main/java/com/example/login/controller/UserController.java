package com.example.login.controller;

import com.example.login.dto.ProfileRequest;
import com.example.login.dto.ProfileResponse;
import com.example.login.dto.ResetPasswordRequest;
import com.example.login.entity.User;
import com.example.login.service.EmailService;
import com.example.login.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> registerUser(@Valid @RequestBody ProfileRequest user) {
        ProfileResponse response=userService.createUser(user);
        emailService.sendWelcomeEmail(response.getEmail(),response.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return ResponseEntity.ok(email!=null);
    }

    @PostMapping("/send-reset-otp")
    public void sendResetOtp(@RequestParam String email){
        try{
            userService.sendResetOtp(email);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        try{
            userService.resetPassword(resetPasswordRequest.getEmail(),resetPasswordRequest.getNewPassword(),resetPasswordRequest.getOtp());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/send-otp")
    public void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email){
        try{
            userService.sendOtp(email);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public void verifyEmail(@RequestBody Map<String,Object> request, @CurrentSecurityContext(expression = "authentication?.name") String email){
        if(request.get("otp").toString()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing OTP");
        }
        try{
            userService.verifyOtp(email,request.get("otp").toString());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
