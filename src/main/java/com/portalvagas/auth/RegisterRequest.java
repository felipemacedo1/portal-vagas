package com.portalvagas.auth;

import com.portalvagas.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
    
    @NotNull
    private User.Role role;
}