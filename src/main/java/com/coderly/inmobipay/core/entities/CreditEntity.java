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
@With
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double rate;

    @Column(name = "amount_payments")
    private Integer amountPayments;

    @Column(name = "loan_amount")
    private BigDecimal loanAmount;

    @Column(name = "lien_insurance")
    private Double lienInsurance;

    @Column(name = "all_risk_insurance")
    private Double allRiskInsurance;

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


}

