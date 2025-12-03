package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.LoginRequest;
import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.response.AuthResponse;
import com.accuresoftech.abc.entity.auth.RefreshToken;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.security.CustomUserDetailsService;
import com.accuresoftech.abc.security.JwtProvider;
import com.accuresoftech.abc.services.RefreshTokenService;
import com.accuresoftech.abc.services.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterUserRequest req) {
        return ResponseEntity.ok(userService.createAdmin(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            UserDetails ud = userDetailsService.loadUserByUsername(req.getEmail());
            String accessToken = jwtProvider.generateToken(ud);

            User user = userRepository.findByEmail(req.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String refreshToken = refreshTokenService.createRefreshToken(user.getId());

            AuthResponse.UserSummary us = AuthResponse.UserSummary.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .roleName(user.getRole().getKey().name())
                    .roleId(user.getRole().getId())
                    .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                    .build();

            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .user(us)
                            .build()
            );

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("{\"error\":\"Invalid credentials\"}");
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {

        String raw = body.get("refreshToken");
        if (raw == null) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"refreshToken required\"}");
        }

        if (!refreshTokenService.validate(raw)) {
            return ResponseEntity.status(401).body("{\"error\":\"Invalid or expired refresh token\"}");
        }

        Long userId = refreshTokenService.findUserId(raw);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Rotate refresh token
        String newRefreshToken = refreshTokenService.rotateToken(raw, userId);

        UserDetails ud = userDetailsService.loadUserByUsername(user.getEmail());
        String newAccessToken = jwtProvider.generateToken(ud);

        AuthResponse.UserSummary us = AuthResponse.UserSummary.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roleName(user.getRole().getKey().name())
                .roleId(user.getRole().getId())
                .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .build();

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .user(us)
                        .build()
        );
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {

        String raw = body.get("refreshToken");
        if (raw == null) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"refreshToken required\"}");
        }

        refreshTokenService.revoke(raw);

        return ResponseEntity.ok("{\"message\":\"Logged out\"}");
    }

}
