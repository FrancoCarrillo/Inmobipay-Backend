package com.coderly.inmobipay.api.model.responses;

import com.coderly.inmobipay.core.entities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetCreditInformationResponse {

    private Long id;
    private String name;
    private double rate;
    private Integer amountPayments;
    private double loanAmount;
    private Boolean isGoodPayerBonus;
    private Boolean isGreenBonus;
    private double propertyValue;
    private double lienInsurance;
    private double allRiskInsurance;
    private Boolean isPhysicalShipping;
    private GracePeriodEntity gracePeriod;
    private InterestRateEntity interestRate;
    private CurrencyEntity currency;
    private BankEntity bank;

}
