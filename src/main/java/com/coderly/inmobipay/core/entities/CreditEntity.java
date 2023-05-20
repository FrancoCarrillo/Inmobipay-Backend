package com.coderly.inmobipay.core.entities;

import javax.persistence.*;

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

    @Column(name = "loan_amount")
    private double loanAmount;

    @Column(name = "is_good_payer_bonus")
    private Boolean isGoodPayerBonus;

    @Column(name = "is_green_bonus")
    private Boolean isGreenBonus;

    @Column(name = "property_value")
    private double propertyValue;

    @Column(name = "lien_insurance")
    private double lienInsurance;

    @Column(name = "all_risk_insurance")
    private double allRiskInsurance;

    @Column(name = "is_physical_shipping")
    private Boolean isPhysicalShipping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grace_period_id")
    private GracePeriodEntity gracePeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_rate_id")
    private InterestRateEntity interestRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private BankEntity bank;


}

