package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import com.coderly.inmobipay.utils.models.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "credit")
@AllArgsConstructor
@Tag(name = "Credit")
public class CreditController {
    private final ICreditService creditService;

    @ApiResponse(responseCode = "400", description = "When the request have a invalid field, the API response this ", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    })
    @Operation(summary = "Save in system a credit information", security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<String> saveCreditInformation(@Valid @RequestBody CreditRequest creditRequest) {
        return ResponseEntity.ok(creditService.create(creditRequest));
    }
    @Operation(summary = "Get payment schedule", security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping
    public ResponseEntity<List<CreditResponses>> getPaymentSchedule(CreditRequest request) {
        return ResponseEntity.ok(creditService.getMonthlyPayment(request));
    }
}
