package com.coderly.inmobipay.infraestructure.interfaces;

import com.coderly.inmobipay.core.entities.CurrencyEntity;

import java.util.List;

public interface ICurrencyService {
    List<CurrencyEntity> getAllCurrencies();
}
