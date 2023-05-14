package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.core.entities.*;
import com.coderly.inmobipay.core.repositories.*;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import com.coderly.inmobipay.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class CreditService implements ICreditService {

    private final CreditRepository creditRepository;
    private final UserRepository userRepository;
    private final GracePeriodRepository gracePeriodRepository;
    private final InterestRateRepository interestRateRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public String create(CreditRequest request) {

        try {
            UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(() -> new NotFoundException("User doesn't exist"));
            InterestRateEntity interestRate = interestRateRepository.findByType(request.getInterestRateType()).orElseThrow(() -> new NotFoundException("Interested rate doesn't exist"));
            CurrencyEntity currency = currencyRepository.findByName(request.getCurrencyName()).orElseThrow(() -> new NotFoundException("Currency doesn't exist"));

            GracePeriodEntity savedGracePeriod = GracePeriodEntity.builder()
                    .amountMonths(request.getAmountPayments())
                    .isTotal(request.getIsTotal())
                    .isPartial(request.getIsPartial())
                    .build();

            GracePeriodEntity gracePeriod = gracePeriodRepository.save(savedGracePeriod);

            CreditEntity savedCredit = CreditEntity.builder()
                    .rate(request.getRate())
                    .amountPayments(request.getAmountPayments())
                    .loanAmount(request.getLoanAmount())
                    .lienInsurance(request.getLienInsurance())
                    .allRiskInsurance(request.getAllRiskInsurance())
                    .isPhysicalShipping(request.getIsPhysicalShipping())
                    .user(user)
                    .gracePeriod(gracePeriod)
                    .interestRate(interestRate)
                    .currency(currency)
                    .build();

            creditRepository.save(savedCredit);
        } catch (Exception e) {
            throw new RuntimeException("The operation failed", e);
        }


        return "Credit data saved successfully!!";
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
