package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.core.entities.*;
import com.coderly.inmobipay.core.repositories.*;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import com.coderly.inmobipay.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public List<CreditResponses> getMonthlyPayment(CreditRequest request) {

//        return getSchedulePaymentOfScotiabank(request);
        return getSchedulePaymentOfBCP(request);
    }

    @Override
    public CreditResponses read(Long aLong) {
        return null;
    }

    @Override
    public CreditResponses update(CreditRequest request, Long aLong) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    private List<CreditResponses> getSchedulePaymentOfScotiabank(CreditRequest request) {
        List<CreditResponses> creditResponsesList = new ArrayList<>();

        double tem = Math.pow((1 + (request.getRate() / 100)), (double) 1 / 12) - 1;
        double allRiskInsurance = (request.getAllRiskInsurance() / 100) * request.getPropertyValue();
        double commission = request.getIsPhysicalShipping() ? 11.0 : 0;


        for (short i = 0; i < request.getAmountPayments(); i++) {
            double periodInterest = (Math.pow(1 + tem, 1) - 1) * request.getLoanAmount();
            double lienInsurance = request.getLoanAmount() * request.getLienInsurance() / 100;

            double amortization = ((request.getLoanAmount() * tem) / (1 - Math.pow(1 + tem, -(request.getAmountPayments() - i)))) - periodInterest;
            double fee = amortization + periodInterest + lienInsurance + allRiskInsurance + commission;

            creditResponsesList.add(CreditResponses
                    .builder()
                    .id(i + 1)
                    .initialBalance(request.getLoanAmount())
                    .amortization(amortization)
                    .interest(periodInterest)
                    .lien_insurance(lienInsurance)
                    .allRiskInsurance(allRiskInsurance)
                    .commission(commission)
                    .fee(fee)
                    .build());

            request.setLoanAmount(request.getLoanAmount() - amortization);

        }

        return creditResponsesList;
    }


    private List<CreditResponses> getSchedulePaymentOfBCP(CreditRequest request) {

        List<CreditResponses> creditResponsesList = new ArrayList<>();

        float tna = (float) ((Math.pow((1 + (request.getRate()) / 100), (1 / 12f)) - 1) * 12 * (365 / 360f));
        float i = (tna / 365) * 30;

        float annualLienInsurance = (float) ((request.getLienInsurance() / 100) * 12);
        float d = (annualLienInsurance / 365) * 30;

        float monthlyAllRiskInsurance = (float) ((request.getAllRiskInsurance() / 100) * 12);
        float n = (monthlyAllRiskInsurance / 365) * 30;
        float allRiskValue = (float) (request.getPropertyValue() * n);


        for (short j = 0; j < request.getAmountPayments(); j++) {
            float monthlyInterest = (float) (request.getLoanAmount() * i);
            float monthlyLienInsurance = (float) (request.getLoanAmount() * d);
            float amortization = (float) (request.getLoanAmount() * (i / (1 - Math.pow((1 + i), -(request.getAmountPayments() - j))))) - monthlyInterest;

            float fee = (float) (monthlyInterest + monthlyLienInsurance + allRiskValue + 3.5 + amortization);

            CreditResponses response = CreditResponses.builder()
                    .id(j + 1)
                    .initialBalance(request.getLoanAmount())
                    .amortization(amortization)
                    .interest(monthlyInterest)
                    .lien_insurance(monthlyLienInsurance)
                    .allRiskInsurance(allRiskValue)
                    .commission(3.5)
                    .fee(fee)
                    .build();

            creditResponsesList.add(response);

            request.setLoanAmount(request.getLoanAmount() - amortization);
        }

        return creditResponsesList;

    }
}
