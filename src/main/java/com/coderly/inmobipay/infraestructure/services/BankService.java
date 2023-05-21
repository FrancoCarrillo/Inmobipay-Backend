package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.core.entities.BankEntity;
import com.coderly.inmobipay.core.entities.CurrencyEntity;
import com.coderly.inmobipay.core.repositories.BankRepository;
import com.coderly.inmobipay.infraestructure.interfaces.IBankService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class BankService implements IBankService {

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

    @Override
    public List<BankEntity> getAllBanks() {
        return bankRepository.findAll();
    }
}
