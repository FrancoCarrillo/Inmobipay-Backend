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
    private Integer id;
    private double initialBalance;
    private double amortization;
    private double interest;
    private double lien_insurance;
    private double allRiskInsurance;
    private double commission;
    private double fee;
}
