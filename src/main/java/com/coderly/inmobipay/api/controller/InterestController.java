package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.core.entities.BankEntity;
import com.coderly.inmobipay.core.entities.InterestRateEntity;
import com.coderly.inmobipay.infraestructure.interfaces.IInterestRateService;
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
@RequestMapping(path = "interest")
@AllArgsConstructor
@Tag(name = "Interest Rate")
public class InterestController {

    IInterestRateService rateService;

    @Operation(summary = "Get all interests rate", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<InterestRateEntity>> getAll() {
        return ResponseEntity.ok(rateService.getAllInterestType());
    }
}
