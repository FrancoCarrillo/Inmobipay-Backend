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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "grace_period_id", nullable = false)
    private GracePeriodEntity gracePeriod;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "interest_rate_id", nullable = false)
    private InterestRateEntity interestRate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "bank_id", nullable = false)
    private BankEntity bank;


}

