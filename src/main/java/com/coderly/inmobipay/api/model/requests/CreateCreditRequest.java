package com.coderly.inmobipay.api.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateCreditRequest {
    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull
    @DecimalMin(value = "0.001", inclusive = false, message = "Invalid rate value")
    private double rate;

    @NotNull
    private Integer amountPayments;

    @NotNull
    @DecimalMin(value = "0.001", inclusive = false, message = "Invalid property value value")
    private double propertyValue;

    @NotNull
    @DecimalMin(value = "0.001", inclusive = false, message = "Invalid loan amount value")
    private double loanAmount;

    @NotNull
    @DecimalMin(value = "0.001", inclusive = false, message = "Invalid lien insurance value")
    private double lienInsurance;

    @NotNull
    @DecimalMin(value = "0.001", inclusive = false, message = "Invalid all risk insurance value")
    private double allRiskInsurance;

    @NotNull
    private Boolean isPhysicalShipping;

    @NotNull
    private Boolean isTotal;

    @NotNull
    private Boolean isPartial;

    private Integer monthlyGracePeriod;

    @NotNull
    @NotEmpty
    private String interestRateType;

    @NotNull
    @NotEmpty
    private String currencyName;

    @NotNull
    @NotEmpty
    private String bankName;

    @NotNull
    private Boolean isGoodPayerBonus;

    @NotNull
    private Boolean isGreenBonus;

    @NotNull
    private Long userId;
}
