package com.coderly.inmobipay.infraestructure.interfaces;

import com.coderly.inmobipay.core.entities.BankEntity;

import java.util.List;

public interface IBankService {
    List<BankEntity> getAllBanks();
}
