package com.coderly.inmobipay.api.model.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreditRequest {

    @NotNull
    @DecimalMin(value = "0.001", inclusive = false, message = "Invalid rate value")
    private double rate;

    @Min(value = 60, message = "The minimum amount payments is 60 months")
    @Max(value = 300, message = "The maximum minimum amount payments is 300 months")
    @NotNull
    private Integer amountPayments;

    @Min(value = 65200, message = "The property value must be greater than 65200")
    @Max(value = 464200, message = "The property value must be less than 464200")
    @NotNull
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

    @Min(value = 1, message = "The minimum monthly grace period is 1 month")
    @Max(value = 6, message = "The maximum monthly grace period is 6 months")
    private Integer monthlyGracePeriod;

    @NotNull
    @NotEmpty
    private String interestRateType;

    @NotNull
    @NotEmpty
    private String currencyName;

    @NotNull
    @NotEmpty
    private String bank;

    @NotNull
    private Boolean isGoodPayerBonus;

    @NotNull
    private Boolean isGreenBonus;
}
