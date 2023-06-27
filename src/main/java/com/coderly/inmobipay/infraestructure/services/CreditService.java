package com.coderly.inmobipay.infraestructure.services;

import com.coderly.inmobipay.api.model.requests.CreateCreditRequest;
import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.requests.GracePeriodRequest;
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

        boolean isDuplicateName = user.getCredits()
                .stream()
                .anyMatch(c -> c.getName().equals(request.getName()));

        if (isDuplicateName)
            throw new NotFoundException(String.format("Credito con el nombre de %s ya existe", request.getName()));


        try {

            CreditEntity savedCredit = CreditEntity.builder()
                    .name(request.getName())
                    .rate(request.getRate())
                    .amountPayments(request.getAmountPayments())
                    .propertyValue(request.getPropertyValue())
                    .loanAmount(request.getLoanAmount())
                    .lienInsurance(request.getLienInsurance())
                    .allRiskInsurance(request.getAllRiskInsurance())
                    .postage(request.getPostage())
                    .commissions(request.getCommissions())
                    .administrativeExpenses(request.getAdministrativeExpenses())
                    .interestRate(interestRate)
                    .isGoodPayerBonus(request.getIsGoodPayerBonus())
                    .isGreenBonus(request.getIsGreenBonus())
                    .cokRate(request.getCokRate())
                    .currency(currency)
                    .user(user)
                    .build();

            creditRepository.save(savedCredit);
        } catch (Exception e) {
            throw new RuntimeException("The operation failed", e);
        }


        return "Credito guardado exitosamente!!";
    }

    @Override
    public List<GetCreditInformationResponse> getCreditByUser(Long user_id) {

        if (!userRepository.existsById(user_id))
            throw new NotFoundException(String.format("Usuario con el ID de %s no existe en la base de datos", user_id));

        List<CreditEntity> creditEntityList = creditRepository.findByUserId(user_id);

        List<GetCreditInformationResponse> creditResponsesList = new ArrayList<>();

        creditEntityList.forEach(creditEntity -> {
            creditResponsesList.add(GetCreditInformationResponse.builder()
                    .id(creditEntity.getId())
                    .name(creditEntity.getName())
                    .rate(creditEntity.getRate())
                    .amountPayments(creditEntity.getAmountPayments())
                    .propertyValue(creditEntity.getPropertyValue())
                    .loanAmount(creditEntity.getLoanAmount())
                    .lienInsurance(creditEntity.getLienInsurance())
                    .allRiskInsurance(creditEntity.getAllRiskInsurance())
                    .administrativeExpenses(creditEntity.getAdministrativeExpenses())
                    .postage(creditEntity.getPostage())
                    .commissions(creditEntity.getCommissions())
                    .interestRate(creditEntity.getInterestRate())
                    .isGoodPayerBonus(creditEntity.getIsGoodPayerBonus())
                    .isGreenBonus(creditEntity.getIsGreenBonus())
                    .cokRate(creditEntity.getCokRate())
                    .currency(creditEntity.getCurrency())
                    .build());
        });

        return creditResponsesList;
    }

    @Override
    public String deleteCreditById(Long creditId) {
        try {

            if (!creditRepository.existsById(creditId))
                throw new NotFoundException(String.format("Credito con el id %s no existe en la base de datos", creditId));

            creditRepository.deleteById(creditId);

            return "Credito eliminado exitosamente!!";
        } catch (Exception e) {
            throw new RuntimeException("The operation failed", e);
        }
    }

    @Override
    public GetPaymentScheduleResponse getMonthlyPayment(CreditRequest request) {

        /*
        * JSON DE PRUEBAS
        {
          "rate": 10.5,
          "amountPayments": 240,
          "propertyValue": 200000,
          "loanAmount": 150000,
          "lienInsurance": 0.028,
          "allRiskInsurance": 0.3,
          "isPhysicalShipping": true,
          "interestRateType": "efectiva",
          "isGoodPayerBonus": false,
          "isGreenBonus": false,
          "cokRate": 20
        }
        */

        Set<ConstraintViolation<CreditRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new NotFoundException(violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));

        InterestRateEntity interestRate = interestRateRepository.findByType(request.getInterestRateType()).orElseThrow(() -> new NotFoundException("Tasa de interes no existe!!"));

        // Verify if the rate is nominal or effective
        if (interestRate.getType().equalsIgnoreCase("nominal"))
            request.setRate(convertNominalToEffective(request.getRate()));


        // Verify if the client has a good payer bonus
        request.setLoanAmount(setLoanAmountByGoodPayerBonus(request.getIsGoodPayerBonus(), request.getPropertyValue(), request.getLoanAmount()));

        if (request.getInitialCost() != 0)
            request.setLoanAmount(request.getLoanAmount() + request.getInitialCost());

        // Verify if the loan amount is less than the 90% of property value
        if (request.getLoanAmount() > (request.getPropertyValue() * 0.9) || request.getLoanAmount() < (request.getPropertyValue() * 0.075))
            throw new NotFoundException("El monto del préstamo es mayor al 90% del valor de la propiedad o menor al 7.5%");

        // Verify if the client has a green bonus
        if (request.getIsGreenBonus())
            request.setLoanAmount(request.getLoanAmount() - 5400);

        // Do the payment schedule
        return getSchedulePaymentOfInterbank(request);


    }

    @Override
    public GetPaymentScheduleResponse getMonthlyPaymentGracePeriod(GracePeriodRequest request) {
        Set<ConstraintViolation<GracePeriodRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new NotFoundException(violations.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));

        // Verify if the client has a good payer bonus
        request.setLoanAmount(setLoanAmountByGoodPayerBonus(request.getIsGoodPayerBonus(), request.getPropertyValue(), request.getLoanAmount()));

        // Verify if the loan amount is less than the 90% of property value
        if (request.getLoanAmount() > (request.getPropertyValue() * 0.9) || request.getLoanAmount() < (request.getPropertyValue() * 0.075))
            throw new NotFoundException("El monto del préstamo es mayor al 90% del valor de la propiedad o menor al 7.5%");

        // Verify if the client has a green bonus
        if (request.getIsGreenBonus())
            request.setLoanAmount(request.getLoanAmount() - 5400);

        // Do the payment schedule
        return getMonthlyPaymentByGracePeriod(request);
    }

    private GetPaymentScheduleResponse getSchedulePaymentOfInterbank(CreditRequest request) {

        List<CreditResponses> creditResponsesList = new ArrayList<>();
        List<Double> flow = new ArrayList<>();

        //Converter COK annual to monthly
        double monthlyCok = getMonthlyCok(request.getCokRate());

        //Converter Effective Rate annual to monthly
        double monthlyEffectiveRate = getMonthlyEffectiveRate(request.getRate());

        double loanAmount = request.getLoanAmount();

        double monthlyAllRiskInsurance = request.getPropertyValue() * ((request.getAllRiskInsurance() / 100) / 12);
        double monthlyInterestRate = monthlyEffectiveRate + (request.getLienInsurance() / 100);

        double van = 0.00;
        int vanPosition = 0;
        van += getActualValueToVanOperation(vanPosition++, monthlyCok, loanAmount);
        flow.add(loanAmount);

        for (short i = 0; i < request.getAmountPayments(); i++) {
            double monthlyInterest = loanAmount * monthlyEffectiveRate;
            double monthlyLienInsurance = loanAmount * (request.getLienInsurance() / 100);

            double fee = loanAmount * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -(request.getAmountPayments() - i))));

            double amortization = fee - monthlyInterest - monthlyLienInsurance;
            double monthlyFee = fee + monthlyAllRiskInsurance + request.getCommissions() + request.getPostage() + request.getAdministrativeExpenses();

            creditResponsesList.add(CreditResponses
                    .builder()
                    .id(i + 1L)
                    .tea(roundSevenDecimals(request.getRate()))
                    .tem(roundSevenDecimals(monthlyEffectiveRate * 100))
                    .gracePeriod("S")
                    .initialBalance(roundTwoDecimals(loanAmount))
                    .amortization(roundTwoDecimals(amortization))
                    .interest(roundTwoDecimals(monthlyInterest))
                    .lien_insurance(roundTwoDecimals(monthlyLienInsurance))
                    .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                    .commission(roundTwoDecimals(request.getCommissions()))
                    .postage(roundTwoDecimals(request.getPostage()))
                    .administrativeExpenses(request.getAdministrativeExpenses())
                    .fee(roundTwoDecimals(monthlyFee))
                    .finalBalance(roundTwoDecimals(loanAmount - amortization))
                    .build());

            loanAmount -= amortization;

            van += getActualValueToVanOperation(vanPosition++, monthlyCok, -monthlyFee);
            flow.add(-monthlyFee);

        }

        return GetPaymentScheduleResponse.builder()
                .creditResponses(creditResponsesList)
                .van(roundTwoDecimals(van))
                .tir(roundSevenDecimals(getTirToOperation(flow)))
                .build();
    }

    public GetPaymentScheduleResponse getMonthlyPaymentByGracePeriod(GracePeriodRequest request) {
        List<CreditResponses> creditResponsesList = new ArrayList<>();
        List<Double> flow = new ArrayList<>();

        if (request.getAmountPayments() != request.getGraceAndRatesRequests().size())
            throw new NotFoundException("El monto de los pagos es diferente al monto de la gracia y tasas");

        //Converter COK annual to monthly
        double monthlyCok = getMonthlyCok(request.getCokRate());


        double loanAmount = request.getLoanAmount();

        double monthlyAllRiskInsurance = request.getPropertyValue() * ((request.getAllRiskInsurance() / 100) / 12);

        double van = 0.00;
        int vanPosition = 0;
        van += getActualValueToVanOperation(vanPosition++, monthlyCok, loanAmount);
        flow.add(loanAmount);

        for (short i = 0; i < request.getAmountPayments(); i++) {

            if (request.getGraceAndRatesRequests().get(i).getGracePeriod().equals("T")) {
                //Converter Effective Rate annual to monthly
                double monthlyEffectiveRate = getMonthlyEffectiveRate(request.getGraceAndRatesRequests().get(i).getTea());

                double monthlyInterest = loanAmount * monthlyEffectiveRate;

                double monthlyLienInsurance = loanAmount * (request.getLienInsurance() / 100);

                double fee = 0;

                double amortization = 0;
                double monthlyFee = fee + monthlyAllRiskInsurance + request.getCommissions() + request.getAdministrativeExpenses() + request.getPostage() + monthlyLienInsurance;

                creditResponsesList.add(CreditResponses
                        .builder()
                        .id(i + 1L)
                        .tea(roundSevenDecimals(request.getGraceAndRatesRequests().get(i).getTea()))
                        .tem(roundSevenDecimals(monthlyEffectiveRate * 100))
                        .gracePeriod("T")
                        .initialBalance(roundTwoDecimals(loanAmount))
                        .amortization(roundTwoDecimals(amortization))
                        .interest(roundTwoDecimals(monthlyInterest))
                        .lien_insurance(roundTwoDecimals(monthlyLienInsurance))
                        .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                        .commission(roundTwoDecimals(request.getCommissions()))
                        .postage(roundTwoDecimals(request.getPostage()))
                        .administrativeExpenses(roundTwoDecimals(request.getAdministrativeExpenses()))
                        .fee(roundTwoDecimals(monthlyFee))
                        .finalBalance(roundTwoDecimals(loanAmount - amortization))
                        .build());

                loanAmount += monthlyInterest;

                van += getActualValueToVanOperation(vanPosition++, monthlyCok, -monthlyFee);
                flow.add(-monthlyFee);
            } else if (request.getGraceAndRatesRequests().get(i).getGracePeriod().equals("P")) {

                //Converter Effective Rate annual to monthly
                double monthlyEffectiveRate = getMonthlyEffectiveRate(request.getGraceAndRatesRequests().get(i).getTea());

                double monthlyInterest = loanAmount * monthlyEffectiveRate;
                double monthlyLienInsurance = loanAmount * (request.getLienInsurance() / 100);

                double fee = monthlyInterest;

                double amortization = 0;
                double monthlyFee = fee + monthlyAllRiskInsurance + request.getCommissions() + request.getAdministrativeExpenses() + request.getPostage() + monthlyLienInsurance;

                creditResponsesList.add(CreditResponses
                        .builder()
                        .id(i + 1L)
                        .tea(roundSevenDecimals(request.getGraceAndRatesRequests().get(i).getTea()))
                        .tem(roundSevenDecimals(monthlyEffectiveRate * 100))
                        .gracePeriod("P")
                        .initialBalance(roundTwoDecimals(loanAmount))
                        .amortization(roundTwoDecimals(amortization))
                        .interest(roundTwoDecimals(monthlyInterest))
                        .lien_insurance(roundTwoDecimals(monthlyLienInsurance))
                        .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                        .commission(roundTwoDecimals(request.getCommissions()))
                        .postage(roundTwoDecimals(request.getPostage()))
                        .administrativeExpenses(roundTwoDecimals(request.getAdministrativeExpenses()))
                        .fee(roundTwoDecimals(monthlyFee))
                        .finalBalance(roundTwoDecimals(loanAmount - amortization))
                        .build());

                van += getActualValueToVanOperation(vanPosition++, monthlyCok, -monthlyFee);
                flow.add(-monthlyFee);
            } else {
                //Converter Effective Rate annual to monthly
                double monthlyEffectiveRate = getMonthlyEffectiveRate(request.getGraceAndRatesRequests().get(i).getTea());
                double monthlyInterestRate = monthlyEffectiveRate + (request.getLienInsurance() / 100);

                double monthlyInterest = loanAmount * monthlyEffectiveRate;
                double monthlyLienInsurance = loanAmount * (request.getLienInsurance() / 100);

                double fee = loanAmount * (monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -(request.getAmountPayments() - i))));

                double amortization = fee - monthlyInterest - monthlyLienInsurance;
                double monthlyFee = fee + monthlyAllRiskInsurance + request.getCommissions() + request.getAdministrativeExpenses() + request.getPostage();

                creditResponsesList.add(CreditResponses
                        .builder()
                        .id(i + 1L)
                        .tea(roundSevenDecimals(request.getGraceAndRatesRequests().get(i).getTea()))
                        .tem(roundSevenDecimals(monthlyEffectiveRate * 100))
                        .gracePeriod("S")
                        .initialBalance(roundTwoDecimals(loanAmount))
                        .amortization(roundTwoDecimals(amortization))
                        .interest(roundTwoDecimals(monthlyInterest))
                        .lien_insurance(roundTwoDecimals(monthlyLienInsurance))
                        .allRiskInsurance(roundTwoDecimals(monthlyAllRiskInsurance))
                        .commission(roundTwoDecimals(request.getCommissions()))
                        .postage(roundTwoDecimals(request.getPostage()))
                        .administrativeExpenses(roundTwoDecimals(request.getAdministrativeExpenses()))
                        .fee(roundTwoDecimals(monthlyFee))
                        .finalBalance(roundTwoDecimals(loanAmount - amortization))
                        .build());

                loanAmount -= amortization;

                van += getActualValueToVanOperation(vanPosition++, monthlyCok, -monthlyFee);
                flow.add(-monthlyFee);
            }
        }
        return GetPaymentScheduleResponse.builder()
                .creditResponses(creditResponsesList)
                .van(roundTwoDecimals(van))
                .tir(roundSevenDecimals(getTirToOperation(flow)))
                .build();
    }

    private double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.parseDouble(twoDForm.format(d));
    }

    private double roundSevenDecimals(double d) {
        DecimalFormat sevenDForm = new DecimalFormat("#.#######");
        return Double.parseDouble(sevenDForm.format(d));
    }

    private double setLoanAmountByGoodPayerBonus(boolean isGoodPayerBonus, double propertyValue, double loanAmount) {
        if (isGoodPayerBonus) {
            if (propertyValue < 93100 && propertyValue > 65200)
                return loanAmount - 25700;
            else if (propertyValue < 139400 && propertyValue > 93100)
                return loanAmount - 214000;
            else if (propertyValue < 232200 && propertyValue > 139400)
                return loanAmount - 19600;
            else if (propertyValue < 343900 && propertyValue > 232200)
                return loanAmount - 10800;
        }
        return loanAmount;
    }

    private double getMonthlyCok(double cok) {
        double annualCok = cok / 100;
        return Math.pow((1 + annualCok), ((double) 1 / 12)) - 1;
    }

    private double getMonthlyEffectiveRate(double rate) {
        double dailyEffectiveRate = Math.pow((1 + (rate / 100)), ((double) 1 / 360)) - 1;
        return Math.pow((1 + dailyEffectiveRate), 30) - 1;
    }

    private double convertNominalToEffective(double nominalRate) {
        return (Math.pow((1 + ((nominalRate / 100) / 360)), 360) - 1) * 100;
    }

    private double getActualValueToVanOperation(Integer actualPositionOfPeriod, double monthlyCok, double flowValue) {
        return flowValue / Math.pow((1 + monthlyCok), actualPositionOfPeriod);
    }

    private double getTirToOperation(List<Double> flow) {
        double minCok = 0.0;
        double maxCok = 24.0;
        double tir;

        while (true) {
            double cok = (minCok + maxCok) / 2;
            double van = 0.0;

            for (int i = 0; i < flow.size(); i++) {
                van += getActualValueToVanOperation(i, cok / 100, flow.get(i));
            }
            if (van < 0) {
                minCok = cok;
            } else if (van > 0) {
                maxCok = cok;
            }

            if (Math.abs(van) < 0.00000001) {
                tir = cok;
                return tir;
            }
        }
    }
}
