package com.coderly.inmobipay.api.model.requests;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;

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
    private Double rate;
    private Integer amountPayments;
    private BigDecimal loanAmount;
    private Double lienInsurance;
    private Double allRiskInsurance;
    private Boolean isPhysicalShipping;
    private Long userId;
    private Boolean isTotal;
    private Boolean isPartial;
    private String interestRateType;
    private String currencyName;
}
