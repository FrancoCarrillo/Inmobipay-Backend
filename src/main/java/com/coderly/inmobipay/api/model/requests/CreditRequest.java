package com.coderly.inmobipay.api.model.requests;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.coderly.inmobipay.core.entities.CurrencyEntity;
import com.coderly.inmobipay.core.entities.GracePeriodEntity;
import com.coderly.inmobipay.core.entities.InterestRateEntity;
import com.coderly.inmobipay.core.entities.UserEntity;
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

    @NotNull
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
    private double loanAmount;
    @NotNull
    private double lienInsurance;
    @NotNull
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
    private String interestRateType;
    @NotNull
    private String currencyName;
    @NotNull
    private String bank;
    @NotNull
    private Boolean isGoodPayerBonus;
    @NotNull
    private Boolean isGreenBonus;
}
