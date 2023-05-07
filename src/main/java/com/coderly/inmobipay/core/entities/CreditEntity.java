package com.coderly.inmobipay.core.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity(name = "credit")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;
    private BigDecimal PropertyValue;
    private Integer LoanTerm;
    private LocalDate DisbursementDate;

}
