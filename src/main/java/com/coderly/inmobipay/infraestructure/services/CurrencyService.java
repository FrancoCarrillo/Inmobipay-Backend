package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.core.entities.CurrencyEntity;
import com.coderly.inmobipay.core.repositories.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class CurrencyService {
    private CurrencyRepository currencyRepository;

    private static final CurrencyEntity[] DEFAULT_NAME = {
            new CurrencyEntity().withName("Dollar").withExchange_value(3.65),
            new CurrencyEntity().withName("Sol").withExchange_value(1D)
    };

    public void seedCurrency() {
        Arrays.stream(DEFAULT_NAME).forEach(currency -> {
            if (!currencyRepository.existsByName(currency.getName())) {
                currencyRepository.save(currency);
            }
        });

    }
}
