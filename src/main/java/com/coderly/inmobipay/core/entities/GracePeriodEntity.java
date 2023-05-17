package com.coderly.inmobipay.core.entities;

import lombok.*;

import javax.persistence.*;

@Entity(name = "grace_period")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GracePeriodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_months")
    private Integer amountMonths;

    @Column(name = "is_total")
    private Boolean isTotal;

    @Column(name = "is_partial")
    private Boolean isPartial;
}
