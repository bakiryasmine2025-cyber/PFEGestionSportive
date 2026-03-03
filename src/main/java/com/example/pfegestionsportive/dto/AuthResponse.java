package com.example.pfegestionsportive.dto;

import com.example.pfegestionsportive.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long    id;
    private String  username;
    private String  email;
    private Role    role;
    private boolean emailVerified;
    private String  message;
    private String  token;


    public AuthResponse(Long id, String username, String email,
                        Role role, boolean emailVerified, String message) {
        this.id            = id;
        this.username      = username;
        this.email         = email;
        this.role          = role;
        this.emailVerified = emailVerified;
        this.message       = message;
        this.token         = null;
    }
}