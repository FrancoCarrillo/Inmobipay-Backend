package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.api.model.requests.CreateCreditRequest;
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
import java.text.DecimalFormat;
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
    public String create(CreateCreditRequest request) {

        // TODO: AGREGAR CAMPOS FALTANTES A LA TABLA DE CREDITOS
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

        // TODO: VALIDAR LA MONEDA DE SOL A DOLARES
        // TODO: TASA DE INTERES CONVERTIR SI ES NOMINAL
        // TODO: REALIZAR EL PERIODO DE GRACIA

        /*
        * JSON DE PRUEBAS {
              "rate": 10.5,
              "amountPayments": 240,
              "propertyValue": 200000,
              "loanAmount": 150000,
              "lienInsurance": 0.0280,
              "allRiskInsurance": 0.30,
              "isPhysicalShipping": false,
              "monthsGracePeriod": 0,
              "isTotal": false,
              "isPartial": false,
              "interestRateType": "efectiva",
              "currencyName": "sol",
              "bank": "interbank",
              "isGoodPayerBonus": true,
              "isGreenBonus": true
            }
        */

        if (request.getIsGoodPayerBonus()) {
            if (request.getPropertyValue() < 93100 && request.getPropertyValue() > 65200)
                request.setLoanAmount(request.getLoanAmount() - 25700);
            else if (request.getPropertyValue() < 139400 && request.getPropertyValue() > 93100)
                request.setLoanAmount(request.getLoanAmount() - 214000);
            else if (request.getPropertyValue() < 232200 && request.getPropertyValue() > 139400)
                request.setLoanAmount(request.getLoanAmount() - 19600);
            else if (request.getPropertyValue() < 343900 && request.getPropertyValue() > 232200)
                request.setLoanAmount(request.getLoanAmount() - 10800);
        }

        if (request.getIsGreenBonus())
            request.setLoanAmount(request.getLoanAmount() - 5400);

        if (request.getBank().equalsIgnoreCase("interbank")) {
            return getSchedulePaymentOfInterbank(request);
        } else if (request.getBank().equalsIgnoreCase("bcp")) {
            return getSchedulePaymentOfBCP(request);
        } else {
            throw new NotFoundException("El banco seleccionado no existe en el sistema");
        }

    }

    private List<CreditResponses> getSchedulePaymentOfInterbank(CreditRequest request) {
        List<CreditResponses> creditResponsesList = new ArrayList<>();

        double dailyEffectiveRate = Math.pow((1 + (request.getRate() / 100)), ((double) 1 / 360)) - 1;
        double monthlyEffectiveRate = Math.pow((1 + dailyEffectiveRate), 30) - 1;
        double loanAmount = request.getLoanAmount();
        double monthlyAllRiskInsurance = request.getPropertyValue() * ((request.getAllRiskInsurance() / 100) / 12);
        double monthlyPhysicalShipping = request.getIsPhysicalShipping() ? 11.00 : 0.00;

        for (short i = 0; i < request.getAmountPayments(); i++) {
            double monthlyInterest = loanAmount * monthlyEffectiveRate;
            double monthlyLienInsurance = loanAmount * (request.getLienInsurance() / 100);

            double monthlyInterestRate = monthlyEffectiveRate + (request.getLienInsurance() / 100);
            double fee = loanAmount * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -(request.getAmountPayments() - i))));

            double amortization = fee - monthlyInterest - monthlyLienInsurance;
            double monthlyFee = fee + monthlyAllRiskInsurance + monthlyPhysicalShipping;

            creditResponsesList.add(CreditResponses
                    .builder()
                    .id(i + 1)
                    .initialBalance(roundTwoDecimals(loanAmount))
                    .amortization(roundTwoDecimals(amortization))
                    .interest(roundTwoDecimals(monthlyInterest))
                    .lien_insurance(roundTwoDecimals(monthlyLienInsurance))
                    .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                    .commission(roundTwoDecimals(monthlyPhysicalShipping))
                    .fee(roundTwoDecimals(monthlyFee))
                    .build());

            loanAmount -= amortization;

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

    private double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.parseDouble(twoDForm.format(d));
    }
}
