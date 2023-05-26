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
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class SecurityService implements ISecurityService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final Validator validator;

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

        Set<ConstraintViolation<RegisterUserRequest>> violations = validator.validate(registerUserRequest);

        if (!violations.isEmpty())
            throw new NotFoundException(violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));

        if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
            throw new NotFoundException("Username already in use");
        }

        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new NotFoundException("Email is already in use");
        }

        if (userRepository.existsByDni(registerUserRequest.getDni())) {
            throw new NotFoundException("DNI is already in use");
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

        if (Objects.equals(registerUserRequest.getUsername(), "admin")) {

            RoleEntity adminRol = rolRepository.findByName("ADMIN");

            if (adminRol == null) {
                throw new NotFoundException("Rol doesn't exist");
            }

            roles.add(adminRol);
        }

        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public String addRoleAdmin(Long user_id) {
        UserEntity user = userRepository.findById(user_id).orElseThrow(() -> new NotFoundException("User not found"));

        RoleEntity rol = rolRepository.findByName("ADMIN");

        if (rol == null) {
            throw new NotFoundException("Rol doesn't exist");
        }

        user.getRoles().forEach(role -> {
            if (role.getName().equals("ADMIN")) {
                throw new NotFoundException("User already has admin role");
            }
        });

        try {
            user.getRoles().add(rol);
            userRepository.save(user);

        } catch (Exception e) {
            throw new InternalException("Problem adding admin role");
        }


        return "Admin role added successfully!";
    }
}
