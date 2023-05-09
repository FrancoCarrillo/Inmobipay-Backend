package com.coderly.inmobipay.api.controller;

import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import com.coderly.inmobipay.utils.models.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "credit")
@AllArgsConstructor
//@Tag(name = "Credit")
public class CreditController {
    private final ICreditService creditService;

//    @ApiResponse(responseCode = "400", description = "When the request have a invalid field, the API response this ", content = {
//            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
//    })
//    @Operation(summary = "Save in system a credit information")
    @PostMapping
    public ResponseEntity<CreditResponses> post(@Valid @RequestBody CreditRequest creditRequest) {
        return ResponseEntity.ok(creditService.create(creditRequest));
    }

}
