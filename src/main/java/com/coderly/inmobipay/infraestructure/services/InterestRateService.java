package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.core.entities.InterestRateEntity;
import com.coderly.inmobipay.core.entities.RoleEntity;
import com.coderly.inmobipay.core.repositories.InterestRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class InterestRateService {
    private final InterestRateRepository interestRateRepository;

    private static final String[] DEFAULT_INTEREST = {"Effective", "Nominal"};

    public void seedInterestRate() {
        Arrays.stream(DEFAULT_INTEREST).forEach(type -> {
            if(!interestRateRepository.existsByType(type)) {
                interestRateRepository.save(new InterestRateEntity().withType(type));
            }
        } );

    }
}
