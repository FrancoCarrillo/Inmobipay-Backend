package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.api.model.requests.LoginRequest;
import com.coderly.inmobipay.api.model.requests.RegisterUserRequest;
import com.coderly.inmobipay.api.model.responses.LogInResponse;
import com.coderly.inmobipay.core.entities.RoleEntity;
import com.coderly.inmobipay.core.entities.UserEntity;
import com.coderly.inmobipay.core.repositories.RolRepository;
import com.coderly.inmobipay.core.repositories.UserRepository;
import com.coderly.inmobipay.infraestructure.interfaces.ISecurityService;
import com.coderly.inmobipay.utils.exceptions.NotFoundException;
import com.coderly.inmobipay.utils.security.jwt.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
@AllArgsConstructor
public class SecurityService implements ISecurityService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LogInResponse login(LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);
        return new LogInResponse(jwt);
    }

    @Override
    public String register(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
            throw new NotFoundException("Username already in use");
        }

        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new NotFoundException("Email is already in use");
        }

        RoleEntity rol = rolRepository.findByName("USER");

        if (rol == null) {
            throw new NotFoundException("Rol doesn't exist");
        }

        UserEntity user = UserEntity.builder()
                .username(registerUserRequest.getUsername())
                .names(registerUserRequest.getNames())
                .lastNames(registerUserRequest.getLastNames())
                .email(registerUserRequest.getEmail())
                .age(registerUserRequest.getAge())
                .dni(registerUserRequest.getDni())
                .password(encoder.encode(registerUserRequest.getPassword()))
                .build();

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(rol);

        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!";
    }
}
