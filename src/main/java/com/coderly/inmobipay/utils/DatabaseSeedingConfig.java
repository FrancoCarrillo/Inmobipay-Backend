package com.coderly.inmobipay.utils;

import com.coderly.inmobipay.infraestructure.services.BankService;
import com.coderly.inmobipay.infraestructure.services.CurrencyService;
import com.coderly.inmobipay.infraestructure.services.InterestRateService;
import com.coderly.inmobipay.infraestructure.services.RoleService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@AllArgsConstructor
public class DatabaseSeedingConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeedingConfig.class);

    private RoleService roleService;
    private InterestRateService interestRateService;
    private CurrencyService currencyService;
    private BankService bankService;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        String name = event.getApplicationContext().getId();
        logger.info("Starting Database Seeding Process for {} at {}", name, new Timestamp(System.currentTimeMillis()));
        roleService.seedRol();
        interestRateService.seedInterestRate();
        currencyService.seedCurrency();
        bankService.seedBank();
        logger.info("Finished Database Seeding Process for {} at {}", name, new Timestamp(System.currentTimeMillis()));
    }
}
