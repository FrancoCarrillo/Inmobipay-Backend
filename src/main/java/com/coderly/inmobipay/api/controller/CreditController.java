package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.api.model.requests.CreateCreditRequest;
import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.GetPaymentScheduleResponse;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "credit")
@AllArgsConstructor
@Tag(name = "Credit")
public class CreditController {
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
}
