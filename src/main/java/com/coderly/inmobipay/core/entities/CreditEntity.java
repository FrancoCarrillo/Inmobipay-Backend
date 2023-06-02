package com.coderly.inmobipay.core.entities;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "credit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double rate;

    @Column(name = "amount_payments")
    private Integer amountPayments;

    @Column(name = "property_value")
    private double propertyValue;

    @Column(name = "loan_amount")
    private double loanAmount;

    @Column(name = "lien_insurance")
    private double lienInsurance;

    @Column(name = "all_risk_insurance")
    private double allRiskInsurance;

    @Column(name = "administrative_expenses")
    private double administrativeExpenses;

    @Column(name = "postage")
    private double postage;

    @Column(name = "commissions")
    private double commissions;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "interest_rate_id", nullable = false)
    private InterestRateEntity interestRate;

    @Column(name = "is_good_payer_bonus")
    private Boolean isGoodPayerBonus;

    @Column(name = "is_green_bonus")
    private Boolean isGreenBonus;

    @Column(name = "cok_rate")
    private double cokRate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}

