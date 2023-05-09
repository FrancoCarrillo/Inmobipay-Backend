package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.api.model.requests.LoginRequest;
import com.coderly.inmobipay.api.model.requests.RegisterUserRequest;
import com.coderly.inmobipay.api.model.responses.LogInResponse;
import com.coderly.inmobipay.core.entities.UserEntity;
import com.coderly.inmobipay.core.repositories.UserRepository;
import com.coderly.inmobipay.utils.security.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("auth/login")
    public ResponseEntity<LogInResponse> login(@RequestBody LoginRequest loginRequest){

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new LogInResponse(jwt));
    }

    @PostMapping("auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserRequest signUpRequest) {

        // Check 1: username
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        // Check 2: email
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        UserEntity user = UserEntity.builder()
                .username(signUpRequest.getUsername())
                .names(signUpRequest.getNames())
                .lastNames(signUpRequest.getLastNames())
                .email(signUpRequest.getEmail())
                .age(signUpRequest.getAge())
                .dni(signUpRequest.getDni())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}
