package com.example.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Enter valid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
