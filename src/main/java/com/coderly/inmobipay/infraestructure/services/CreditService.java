package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.core.repositories.CreditRepository;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class CreditService implements ICreditService {

    private final CreditRepository creditRepository;

    @Override
    public CreditResponses create(CreditRequest request) {
        return null;
    }

    @Override
    public CreditResponses read(UUID uuid) {
        return null;
    }

    @Override
    public CreditResponses update(CreditRequest request, UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }
}
