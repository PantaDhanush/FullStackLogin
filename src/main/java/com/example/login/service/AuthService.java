package com.example.login.service;

import com.example.login.dto.AuthResponse;
import com.example.login.util.JwtUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtil  jwtUtil;
    public AuthService(AuthenticationManager authenticationManager, AppUserDetailsService appUserDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.appUserDetailsService = appUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(String email, String password) {
        Authentication authToken=new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authToken);
            UserDetails userDetails=appUserDetailsService.loadUserByUsername(email);
            String jwtToken=jwtUtil.generateToken(userDetails);
            ResponseCookie cookie=ResponseCookie.from("jwt",jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60*15)
                    .sameSite("Strict")
                    .build();
            return new AuthResponse(email,jwtToken,cookie);
        }catch (BadCredentialsException e){
            throw new RuntimeException("Invalid email or password");
        }
    }
}
