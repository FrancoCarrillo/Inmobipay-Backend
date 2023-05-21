package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.api.model.requests.LoginRequest;
import com.coderly.inmobipay.api.model.requests.RegisterUserRequest;
import com.coderly.inmobipay.api.model.responses.LogInResponse;
import com.coderly.inmobipay.infraestructure.interfaces.ISecurityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {

    @Operation(summary = "Login in system")
    @PostMapping("auth/login")
    public ResponseEntity<LogInResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.login(loginRequest));
    }
    private final ISecurityService securityService;

    @Operation(summary = "Register in system")
    @PostMapping("auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserRequest signUpRequest) {
        return ResponseEntity.ok(securityService.register(signUpRequest));
    }
}
