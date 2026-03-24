package com.example.login.service;

import com.example.login.dto.ProfileRequest;
import com.example.login.dto.ProfileResponse;
import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ProfileResponse createUser(ProfileRequest profileRequest) {
        User user = convertToEntity(profileRequest);
        if(!userRepository.existsByEmail(profileRequest.getEmail())) {
            return convertToResponse(userRepository.save(user));
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    private ProfileResponse convertToResponse(User savedUser) {
        return ProfileResponse.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .isAccountVerified(savedUser.getIsAccountVerified())
                .build();
    }

    private User convertToEntity(ProfileRequest profileRequest) {
       return User.builder()
                .userId(UUID.randomUUID().toString())
                .name(profileRequest.getName())
                .email(profileRequest.getEmail())
                .password(passwordEncoder.encode(profileRequest.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .resetOtp(null)
                .verifyOtpExpireAt(0L)
                .verifyOtp(null)
                .build();
    }
}
