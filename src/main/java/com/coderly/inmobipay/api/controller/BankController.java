package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.core.entities.BankEntity;
import com.coderly.inmobipay.infraestructure.interfaces.IBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "bank")
@AllArgsConstructor
@Tag(name = "Bank")
public class BankController {

    private final IBankService bankService;

    @Operation(summary = "Get all banks", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<BankEntity>> getAll() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }
}
