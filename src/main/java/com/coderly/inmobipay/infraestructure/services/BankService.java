package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.core.entities.BankEntity;
import com.coderly.inmobipay.core.entities.CurrencyEntity;
import com.coderly.inmobipay.core.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class BankService {

    private final BankRepository bankRepository;

    private static final String[] DEFAULT_NAME = {
            "interbank",
            "bcp"
    };

    public void seedBank() {
        Arrays.stream(DEFAULT_NAME).forEach(bank -> {
            if (!bankRepository.existsByName(bank)) {
                bankRepository.save(BankEntity.builder().name(bank).build());
            }
        });

    }
}
