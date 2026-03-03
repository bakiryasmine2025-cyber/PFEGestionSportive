package com.example.pfegestionsportive.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.Objects;
import com.example.pfegestionsportive.dto.*;
import com.example.pfegestionsportive.model.Role;
import com.example.pfegestionsportive.model.User;
import com.example.pfegestionsportive.repository.UserRepository;
import com.example.pfegestionsportive.Config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setPhone(request.getPhone());
        user.setEmailVerified(false);
        user.setDateNaissance(request.getDateNaissance());
        user.setGenre(request.getGenre());
        user.setNationalite(request.getNationalite());
        user.setAdresse(request.getAdresse());

        String code = generateCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), code);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEmailVerified(),
                "Inscription réussie! Vérifiez votre email."
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email non trouvé"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Veuillez vérifier votre email d'abord");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEmailVerified(),
                "Connexion réussie!",
                token
        );
    }

    public AuthResponse verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email non trouvé"));

        if (user.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà vérifié");
        }

        if (!Objects.equals(request.getCode(), user.getVerificationCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code de vérification incorrect");
        }

        if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code de vérification expiré");
        }

        user.setEmailVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEmailVerified(),
                "Email vérifié avec succès!"
        );
    }

    public String forgotPassword(ForgetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email non trouvé"));

        String code = generateCode();
        user.setResetPasswordCode(code);
        user.setResetPasswordCodeExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), code);
        return "Code de réinitialisation envoyé à votre email!";
    }

    public String resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordCode(request.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Code invalide ou inexistant"));

        if (user.getResetPasswordCodeExpiry() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aucune demande de réinitialisation en cours");
        }

        if (user.getResetPasswordCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.GONE, "Code expiré, demandez un nouveau code");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordCode(null);
        user.setResetPasswordCodeExpiry(null);
        userRepository.save(user);

        return "Mot de passe réinitialisé avec succès! POUR UTILISATEUR " + user.getEmail();
    }

    public String setRole(String email, Role role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User non trouvé"));
        user.setRole(role);
        userRepository.save(user);
        return "Rôle mis à jour: " + role + " pour " + email;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole()))
                .collect(Collectors.toList());
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}