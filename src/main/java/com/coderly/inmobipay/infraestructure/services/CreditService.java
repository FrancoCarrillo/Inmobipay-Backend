package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.api.model.requests.CreateCreditRequest;
import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.api.model.responses.GetCreditInformationResponse;
import com.coderly.inmobipay.api.model.responses.GetPaymentScheduleResponse;
import com.coderly.inmobipay.core.entities.*;
import com.coderly.inmobipay.core.repositories.*;
import com.coderly.inmobipay.infraestructure.interfaces.ICreditService;
import com.coderly.inmobipay.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class CreditService implements ICreditService {

    private final CreditRepository creditRepository;
    private final UserRepository userRepository;
    private final GracePeriodRepository gracePeriodRepository;
    private final InterestRateRepository interestRateRepository;
    private final CurrencyRepository currencyRepository;
    private final BankRepository bankRepository;
    private final Validator validator;

    @Override
    public String create(CreateCreditRequest request) {

        Set<ConstraintViolation<CreateCreditRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new NotFoundException(violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));

        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(() -> new NotFoundException("User doesn't exist"));
        InterestRateEntity interestRate = interestRateRepository.findByType(request.getInterestRateType()).orElseThrow(() -> new NotFoundException("Interested rate doesn't exist"));
        CurrencyEntity currency = currencyRepository.findByName(request.getCurrencyName()).orElseThrow(() -> new NotFoundException("Currency doesn't exist"));
        BankEntity bank = bankRepository.findByName(request.getBankName()).orElseThrow(() -> new NotFoundException("Bank doesn't exist"));

        boolean isDuplicateName = user.getCredits()
                .stream()
                .anyMatch(c -> c.getName().equals(request.getName()));

        if (isDuplicateName)
            throw new NotFoundException(String.format("Credit with %s already exists", request.getName()));


        try {

            GracePeriodEntity savedGracePeriod = GracePeriodEntity.builder()
                    .amountMonths(request.getAmountPayments())
                    .isTotal(request.getIsTotal())
                    .isPartial(request.getIsPartial())
                    .build();

            GracePeriodEntity gracePeriod = gracePeriodRepository.save(savedGracePeriod);

            CreditEntity savedCredit = CreditEntity.builder()
                    .name(request.getName())
                    .rate(request.getRate())
                    .amountPayments(request.getAmountPayments())
                    .loanAmount(request.getLoanAmount())
                    .isGoodPayerBonus(request.getIsGoodPayerBonus())
                    .isGreenBonus(request.getIsGreenBonus())
                    .propertyValue(request.getPropertyValue())
                    .lienInsurance(request.getLienInsurance())
                    .allRiskInsurance(request.getAllRiskInsurance())
                    .isPhysicalShipping(request.getIsPhysicalShipping())
                    .user(user)
                    .gracePeriod(gracePeriod)
                    .interestRate(interestRate)
                    .currency(currency)
                    .bank(bank)
                    .build();

            creditRepository.save(savedCredit);
        } catch (Exception e) {
            throw new RuntimeException("The operation failed", e);
        }


        return "Credit data saved successfully!!";
    }

    @Override
    public List<GetCreditInformationResponse> getCreditByUser(Long user_id) {

        if (!userRepository.existsById(user_id))
            throw new NotFoundException(String.format("User with id %s doesn't exist in the database", user_id));

        List<CreditEntity> creditEntityList = creditRepository.findByUserId(user_id);

        List<GetCreditInformationResponse> creditResponsesList = new ArrayList<>();

        creditEntityList.forEach(creditEntity -> {
            creditResponsesList.add(GetCreditInformationResponse.builder()
                    .id(creditEntity.getId())
                    .name(creditEntity.getName())
                    .rate(creditEntity.getRate())
                    .amountPayments(creditEntity.getAmountPayments())
                    .loanAmount(creditEntity.getLoanAmount())
                    .isGoodPayerBonus(creditEntity.getIsGoodPayerBonus())
                    .isGreenBonus(creditEntity.getIsGreenBonus())
                    .propertyValue(creditEntity.getPropertyValue())
                    .lienInsurance(creditEntity.getLienInsurance())
                    .allRiskInsurance(creditEntity.getAllRiskInsurance())
                    .isPhysicalShipping(creditEntity.getIsPhysicalShipping())
                    .gracePeriod(creditEntity.getGracePeriod())
                    .interestRate(creditEntity.getInterestRate())
                    .currency(creditEntity.getCurrency())
                    .bank(creditEntity.getBank())
                    .build());
        });

        return creditResponsesList;
    }

    @Override
    public String deleteCreditById(Long creditId) {
        try {

            if (!creditRepository.existsById(creditId))
                throw new NotFoundException(String.format("Credit with id %s doesn't exist in the database", creditId));

            creditRepository.deleteById(creditId);

            return "Credit deleted successfully!!";
        } catch (Exception e) {
            throw new RuntimeException("The operation failed", e);
        }
    }

    @Override
    public GetPaymentScheduleResponse getMonthlyPayment(CreditRequest request) {

        // TODO: CALCULAR TIR DE LA OPERACION

        /*
        * JSON DE PRUEBAS
        {
          "rate": 10.5,
          "amountPayments": 240,
          "propertyValue": 200000,
          "loanAmount": 150000,
          "lienInsurance": 0.0280,
          "allRiskInsurance": 0.30,
          "isPhysicalShipping": true,
          "isTotal": false,
          "isPartial": false,
          "monthlyGracePeriod": 6,
          "interestRateType": "effective",
          "currencyName": "sol",
          "bank": "interbank",
          "isGoodPayerBonus": false,
          "isGreenBonus": false
        }
        */

        Set<ConstraintViolation<CreditRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new NotFoundException(violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));

        InterestRateEntity interestRate = interestRateRepository.findByType(request.getInterestRateType()).orElseThrow(() -> new NotFoundException("Interested rate doesn't exist"));
        currencyRepository.findByName(request.getCurrencyName()).orElseThrow(() -> new NotFoundException("Currency doesn't exist"));
        BankEntity bank = bankRepository.findByName(request.getBank()).orElseThrow(() -> new NotFoundException("The system not fount the selected bank"));

        // Verify if the rate is nominal or effective
        if (interestRate.getType().equalsIgnoreCase("nominal"))
            request.setRate(convertNominalToEffective(request.getRate()));


        // Verify if the client has a good payer bonus
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

        // Verify if the loan amount is less than the 90% of property value
        if (request.getLoanAmount() > (request.getPropertyValue() * 0.9) || request.getLoanAmount() < (request.getPropertyValue() * 0.075))
            throw new NotFoundException("The loan amount is greater than the 90% of property value or less than 7.5%");

        // Verify if the client has a green bonus
        if (request.getIsGreenBonus())
            request.setLoanAmount(request.getLoanAmount() - 5400);

        // Do the payment schedule
        if (bank.getName().equalsIgnoreCase("interbank")) {
            return getSchedulePaymentOfInterbank(request);
        } else if (bank.getName().equalsIgnoreCase("bcp")) {
            return getSchedulePaymentOfBCP(request);
        } else {
            throw new NotFoundException("The system not fount the selected bank");
        }

    }

    private GetPaymentScheduleResponse getSchedulePaymentOfInterbank(CreditRequest request) {

        List<CreditResponses> creditResponsesList = new ArrayList<>();
        double annualCok = 20.00 / 100;
        double monthlyCok = Math.pow((1 + annualCok), ((double) 1 / 12)) - 1;

        double dailyEffectiveRate = Math.pow((1 + (request.getRate() / 100)), ((double) 1 / 360)) - 1;
        double monthlyEffectiveRate = Math.pow((1 + dailyEffectiveRate), 30) - 1;

        double loanAmount = request.getLoanAmount();

        double monthlyAllRiskInsurance = request.getPropertyValue() * ((request.getAllRiskInsurance() / 100) / 12);
        double monthlyPhysicalShipping = request.getIsPhysicalShipping() ? 11.00 : 0.00;
        double monthlyInterestRate = monthlyEffectiveRate + (request.getLienInsurance() / 100);

        double van = 0.00;
        int vanPosition = 0;
        van += getActualValueToVanOperation(vanPosition++, monthlyCok, loanAmount);

        for (short i = 0; i < request.getAmountPayments(); i++) {
            double monthlyInterest = loanAmount * monthlyEffectiveRate;
            double monthlyLienInsurance = loanAmount * (request.getLienInsurance() / 100);

            double fee = loanAmount * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -(request.getAmountPayments() - i))));

            double amortization = fee - monthlyInterest - monthlyLienInsurance;
            double monthlyFee = fee + monthlyAllRiskInsurance + monthlyPhysicalShipping;

            //Verify if the client has a total or partial grace period
            if (request.getIsTotal()) {
                if (request.getMonthlyGracePeriod() == null)
                    throw new NotFoundException("The monthly grace period cannot be empty");

                if (request.getIsPartial())
                    throw new NotFoundException("The system doesn't support partial and total grace period at the same time");

                double interestTotalGracePeriod = 0.00;

                for (short j = 0; j < request.getMonthlyGracePeriod(); j++) {

                    double monthlyGracePeriodLienInsurance = loanAmount * (request.getLienInsurance() / 100);

                    interestTotalGracePeriod = monthlyEffectiveRate * loanAmount;

                    creditResponsesList.add(CreditResponses
                            .builder()
                            .id(j + 1L)
                            .initialBalance(roundTwoDecimals(loanAmount))
                            .amortization(0.00)
                            .interest(roundTwoDecimals(interestTotalGracePeriod))
                            .lien_insurance(roundTwoDecimals(monthlyGracePeriodLienInsurance))
                            .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                            .commission(roundTwoDecimals(monthlyPhysicalShipping))
                            .fee(0.00)
                            .build());


                    loanAmount += interestTotalGracePeriod;

                    van += getActualValueToVanOperation(vanPosition++, monthlyCok, -monthlyGracePeriodLienInsurance - monthlyAllRiskInsurance - monthlyPhysicalShipping);

                }

                i = (short) (request.getMonthlyGracePeriod() - 1);

                request.setIsTotal(false);
            } else if (request.getIsPartial()) {
                if (request.getMonthlyGracePeriod() == null)
                    throw new NotFoundException("The monthly grace period cannot be empty");

                var interestTotalGracePeriod = monthlyInterest + monthlyLienInsurance + monthlyAllRiskInsurance + monthlyPhysicalShipping;

                for (short j = 0; j < request.getMonthlyGracePeriod(); j++) {

                    creditResponsesList.add(CreditResponses
                            .builder()
                            .id(j + 1L)
                            .initialBalance(roundTwoDecimals(loanAmount))
                            .amortization(0.00)
                            .interest(roundTwoDecimals(monthlyInterest))
                            .lien_insurance(roundTwoDecimals(monthlyLienInsurance))
                            .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                            .commission(roundTwoDecimals(monthlyPhysicalShipping))
                            .fee(roundTwoDecimals(interestTotalGracePeriod))
                            .build());

                    van += getActualValueToVanOperation(vanPosition++, monthlyCok, -monthlyLienInsurance - monthlyAllRiskInsurance - monthlyPhysicalShipping - interestTotalGracePeriod);

                }

                i = (short) (request.getMonthlyGracePeriod() - 1);

                request.setIsPartial(false);
            } else {
                creditResponsesList.add(CreditResponses
                        .builder()
                        .id(i + 1L)
                        .initialBalance(roundTwoDecimals(loanAmount))
                        .amortization(roundTwoDecimals(amortization))
                        .interest(roundTwoDecimals(monthlyInterest))
                        .lien_insurance(roundTwoDecimals(monthlyLienInsurance))
                        .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                        .commission(roundTwoDecimals(monthlyPhysicalShipping))
                        .fee(roundTwoDecimals(monthlyFee))
                        .build());

                loanAmount -= amortization;

                van += getActualValueToVanOperation(vanPosition++, monthlyCok, -fee - monthlyAllRiskInsurance - monthlyPhysicalShipping);

            }

        }


        return GetPaymentScheduleResponse.builder()
                .creditResponses(creditResponsesList)
                .van(roundTwoDecimals(van))
                .tir(0.00)
                .build();
    }

    private GetPaymentScheduleResponse getSchedulePaymentOfBCP(CreditRequest request) {

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
                    .id(j + 1L)
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

        return GetPaymentScheduleResponse.builder()
                .creditResponses(creditResponsesList)
                .van(0.00)
                .tir(0.00)
                .build();

    }

    private double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.parseDouble(twoDForm.format(d));
    }

    private double convertNominalToEffective(double nominalRate) {
        return (Math.pow((1 + ((nominalRate / 100) / 360)), 360) - 1) * 100;
    }


    private double getActualValueToVanOperation(Integer actualPositionOfPeriod, double monthlyCok, double flowValue) {
        return flowValue / Math.pow((1 + monthlyCok), actualPositionOfPeriod);
    }
}
