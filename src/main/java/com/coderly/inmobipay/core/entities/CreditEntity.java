package com.coderly.inmobipay.core.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "credit")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;
    private BigDecimal PropertyValue;
    private Integer LoanTerm;
    private LocalDate DisbursementDate;

}
