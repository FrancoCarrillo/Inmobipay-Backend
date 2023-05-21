package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.api.model.requests.CreateCreditRequest;
import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.api.model.responses.GetCreditInformationResponse;
import com.coderly.inmobipay.api.model.responses.GetPaymentScheduleResponse;
import com.coderly.inmobipay.core.entities.CreditEntity;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "credit")
@AllArgsConstructor
@Tag(name = "Credit")
public class CreditController {

    //TODO: Get all payment schedule by user
    //TODO: Get all entities services

    private final ICreditService creditService;

    @Operation(summary = "Save in system a credit information", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/save")
    public ResponseEntity<String> saveCreditInformation(@RequestBody CreateCreditRequest creditRequest) {
        return ResponseEntity.ok(creditService.create(creditRequest));
    }

    @Operation(summary = "Get payment schedule of credit information", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/schedule")
    public ResponseEntity<GetPaymentScheduleResponse> getPaymentSchedule(@RequestBody CreditRequest request) {
        return ResponseEntity.ok(creditService.getMonthlyPayment(request));
    }

    @Operation(summary = "Get credit information by user", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<GetCreditInformationResponse>> getCreditById(@PathVariable Long userId) {
        return ResponseEntity.ok(creditService.getCreditByUser(userId));
    }

    @Operation(summary = "Delete credit information", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{creditId}")
    public ResponseEntity<String> deleteCreditById(@PathVariable Long creditId) {
        return ResponseEntity.ok(creditService.deleteCreditById(creditId));
    }

}
