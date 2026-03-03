package com.example.pfegestionsportive.dto;

import com.example.pfegestionsportive.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
}