package com.coderly.inmobipay.infraestructure.interfaces;

import com.coderly.inmobipay.api.model.requests.CreateCreditRequest;
import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ICreditService {
    List<CreditResponses> getMonthlyPayment(CreditRequest request);
    String create(CreateCreditRequest request);
}
