package com.example.login.service;

import com.example.login.dto.ProfileRequest;
import com.example.login.dto.ProfileResponse;
import com.example.login.entity.User;
import com.example.login.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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

    public ProfileResponse getProfile(String email) {
        User existingUser=userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToResponse(existingUser);
    }

    public void sendResetOtp(String email) {
        User existingUser=userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        //Generate 6 digit otp
        String otp=String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        //calculate expiry time (current time+ 15mins in milliseconds)
        long expireTime=System.currentTimeMillis()+(5*60*1000);
        //update the user in database
        existingUser.setResetOtp(otp);
        existingUser.setResetOtpExpireAt(expireTime);
        userRepository.save(existingUser);

        try{
            emailService.sendResetOtpEmail(existingUser.getEmail(), otp);
        }catch (Exception e){
            throw new RuntimeException("Unable to send reset OTP");
        }
    }

    public void resetPassword(String email, String newPassword, String otp) {
        User existingUser=userRepository.findByEmail(email)
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(existingUser.getResetOtp()==null||!existingUser.getResetOtp().equals(otp)){
            throw new RuntimeException("INVALID OTP");
        }

        if(existingUser.getResetOtpExpireAt()<System.currentTimeMillis()){
            throw new RuntimeException("OTP expired");
        }
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(null);
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }
}
