package com.example.login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "New Password is Required")
    private String newPassword;
    @NotBlank(message = "Email is Required")
    private String email;
    @NotBlank(message = "Otp is Required")
    private String otp;
}
