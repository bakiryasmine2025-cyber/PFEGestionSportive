package com.example.pfegestionsportive.controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.pfegestionsportive.model.Role;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.pfegestionsportive.dto.UserDTO;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.pfegestionsportive.dto.AuthResponse;
import com.example.pfegestionsportive.dto.ForgetPasswordRequest;
import com.example.pfegestionsportive.dto.LoginRequest;
import com.example.pfegestionsportive.dto.RegisterRequest;
import com.example.pfegestionsportive.dto.ResetPasswordRequest;
import com.example.pfegestionsportive.dto.VerifyEmailRequest;
import com.example.pfegestionsportive.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request) {
        return ResponseEntity.ok(userService.verifyEmail(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgetPasswordRequest request) {
        return ResponseEntity.ok(userService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }
    @PostMapping("/admin/set-role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> setRole(@RequestParam String email, @RequestParam Role role) {
        return ResponseEntity.ok(userService.setRole(email, role));

    }
    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('FEDERATION_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}