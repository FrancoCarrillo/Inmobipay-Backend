package com.coderly.inmobipay.api.model.requests;

import javax.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreditRequest {
    private BigDecimal PropertyValue;
    @Max(value = 12000, message = "The loan term must be greater than 1200")
    private Integer LoanTerm;
    private LocalDate DisbursementDate;
}
