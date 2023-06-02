package com.coderly.inmobipay.api.model.responses;

import com.coderly.inmobipay.core.entities.CurrencyEntity;
import com.coderly.inmobipay.core.entities.InterestRateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetCreditInformationResponse {

    private Long id;
    private String name;
    private double rate;
    private Integer amountPayments;
    private double propertyValue;
    private double loanAmount;
    private double lienInsurance;
    private double allRiskInsurance;
    private double administrativeExpenses;
    private double postage;
    private double commissions;
    private InterestRateEntity interestRate;
    private Boolean isGoodPayerBonus;
    private Boolean isGreenBonus;
    private double cokRate;
    private CurrencyEntity currency;

}
