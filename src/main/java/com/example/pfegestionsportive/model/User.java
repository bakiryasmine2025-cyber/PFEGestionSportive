package com.example.pfegestionsportive.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private Role role = Role.USER;

    @Builder.Default
    private boolean emailVerified = false;

    private String verificationCode;

    private LocalDateTime verificationCodeExpiry;

    private String resetPasswordCode;

    private LocalDateTime resetPasswordCodeExpiry;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String phone;

    private LocalDate dateNaissance;

    private String genre;

    private String nationalite;

    private String adresse;

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif=true;
}