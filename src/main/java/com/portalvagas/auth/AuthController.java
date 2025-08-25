package com.portalvagas.auth;

import com.portalvagas.domain.User;
import com.portalvagas.domain.UserRepository;
import com.portalvagas.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
            user.getId(), 
            user.getEmail(), 
            user.getRole()
        );

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
            user.getId(), 
            user.getEmail(), 
            user.getRole()
        );

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, userDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        String email = jwtService.extractUsername(request.refreshToken());
        User user = userRepository.findByEmail(email).orElseThrow();
        
        if (jwtService.isTokenValid(request.refreshToken(), user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            
            AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                user.getId(), 
                user.getEmail(), 
                user.getRole()
            );
            
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, userDto));
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public record RefreshRequest(String refreshToken) {}
}