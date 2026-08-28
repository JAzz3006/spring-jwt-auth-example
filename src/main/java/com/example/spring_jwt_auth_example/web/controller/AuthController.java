package com.example.spring_jwt_auth_example.web.controller;

import com.example.spring_jwt_auth_example.exception.AlreadyExistsException;
import com.example.spring_jwt_auth_example.repository.UserRepository;
import com.example.spring_jwt_auth_example.security.SecurityService;
import com.example.spring_jwt_auth_example.web.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> authUser(LoginRequest loginRequest){
        return ResponseEntity.ok(securityService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody CreateUserRequest createUserRequest){
        if (userRepository.existsByUsername(createUserRequest.getUsername())){
            throw new AlreadyExistsException(String.format("Username '%s' already exists",
                    createUserRequest.getUsername()
            ));
        }
        if (userRepository.existsByEmail(createUserRequest.getEmail())){
            throw new AlreadyExistsException(String.format("Email '%s' already exists",
                    createUserRequest.getEmail()
            ));
        }
        securityService.register(createUserRequest);
        return ResponseEntity.ok(new SimpleResponse(
                String.format("User '%s' created", createUserRequest.getUsername())
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(securityService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logoutUser(@AuthenticationPrincipal UserDetails userDetails){
        securityService.logout();
        return ResponseEntity.ok(new SimpleResponse(
                String.format("User '%s' logout", userDetails.getUsername())
            )
        );
    }
}