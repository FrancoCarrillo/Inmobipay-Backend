package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.core.entities.BankEntity;
import com.coderly.inmobipay.core.entities.CurrencyEntity;
import com.coderly.inmobipay.infraestructure.interfaces.IBankService;
import com.coderly.inmobipay.infraestructure.interfaces.ICurrencyService;
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
@RequestMapping(path = "currency")
@AllArgsConstructor
@Tag(name = "Currency")
public class CurrencyController {

    private final ICurrencyService currencyService;

    @Operation(summary = "Get all currencies", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<CurrencyEntity>> getAll() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }
}
