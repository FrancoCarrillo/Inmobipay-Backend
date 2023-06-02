package com.coderly.inmobipay.api.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreditResponses {
    private Long id;
    private double tea;
    private double tem;
    private String gracePeriod;
    private double initialBalance;
    private double interest;
    private double amortization;
    private double lien_insurance;
    private double allRiskInsurance;
    private double commission;
    private double administrativeExpenses;
    private double postage;
    private double fee;
}
