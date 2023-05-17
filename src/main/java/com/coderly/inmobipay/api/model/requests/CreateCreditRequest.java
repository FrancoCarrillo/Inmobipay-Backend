package com.coderly.inmobipay.api.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateCreditRequest {
    private double rate;
    private Integer amountPayments;
    private double propertyValue;
    private double loanAmount;
    private double lienInsurance;
    private double allRiskInsurance;
    private Boolean isPhysicalShipping;
    private Boolean isTotal;
    private Boolean isPartial;
    private Integer monthlyGracePeriod;
    private String interestRateType;
    private String currencyName;
    private String bank;
    private Boolean isGoodPayerBonus;
    private Boolean isGreenBonus;
    private Long userId;
}
