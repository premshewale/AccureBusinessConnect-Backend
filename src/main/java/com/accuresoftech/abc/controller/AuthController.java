package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.LoginRequest;
import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.response.JwtResponse;
import com.accuresoftech.abc.dto.response.UserResponse;
import com.accuresoftech.abc.security.CustomUserDetailsService;
import com.accuresoftech.abc.security.JwtProvider;
import com.accuresoftech.abc.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/register-admin")
    public ResponseEntity<UserResponse> registerAdmin(@Valid @RequestBody RegisterUserRequest req) {
        return ResponseEntity.ok(userService.createAdmin(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
            UserDetails ud = userDetailsService.loadUserByUsername(req.getEmail());
            String token = jwtProvider.generateToken(ud);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\":\"Invalid email or password\"}");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"error\":\"User account is disabled\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Login failed. Please try again later.\"}");
        }
    }
}
