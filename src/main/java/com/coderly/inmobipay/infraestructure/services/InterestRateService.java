package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.core.entities.InterestRateEntity;
import com.coderly.inmobipay.core.repositories.InterestRateRepository;
import com.coderly.inmobipay.infraestructure.interfaces.IInterestRateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class InterestRateService implements IInterestRateService {

    private final InterestRateRepository interestRateRepository;

    private static final String[] DEFAULT_INTEREST = {"Efectiva", "Nominal"};

    public void seedInterestRate() {
        Arrays.stream(DEFAULT_INTEREST).forEach(type -> {
            if (!interestRateRepository.existsByType(type)) {
                interestRateRepository.save(new InterestRateEntity().withType(type));
            }
        });

    }

    @Override
    public List<InterestRateEntity> getAllInterestType() {
        return interestRateRepository.findAll();
    }

}

