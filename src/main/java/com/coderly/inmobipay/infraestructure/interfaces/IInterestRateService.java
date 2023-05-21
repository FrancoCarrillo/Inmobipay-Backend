package com.coderly.inmobipay.infraestructure.interfaces;

import com.coderly.inmobipay.core.entities.InterestRateEntity;

import java.util.List;

public interface IInterestRateService {
    List<InterestRateEntity> getAllInterestType();
}
