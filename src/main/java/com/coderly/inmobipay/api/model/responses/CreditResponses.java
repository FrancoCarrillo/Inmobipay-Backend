package com.coderly.inmobipay.api.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreditResponses {
    private Long Id;
    private BigDecimal PropertyValue;
    private Integer LoanTerm;
    private LocalDate DisbursementDate;
}
