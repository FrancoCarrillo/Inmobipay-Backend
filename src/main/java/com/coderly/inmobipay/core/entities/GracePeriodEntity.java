package com.coderly.inmobipay.core.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "grace_period")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class GracePeriodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer amount_months;

    private Boolean is_total;

    private Boolean is_partial;
}
