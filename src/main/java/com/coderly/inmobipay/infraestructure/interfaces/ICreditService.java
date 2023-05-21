package com.coderly.inmobipay.infraestructure.interfaces;

import com.coderly.inmobipay.api.model.requests.CreateCreditRequest;
import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;
import com.coderly.inmobipay.api.model.responses.GetCreditInformationResponse;
import com.coderly.inmobipay.api.model.responses.GetPaymentScheduleResponse;
import com.coderly.inmobipay.core.entities.CreditEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ICreditService {
    GetPaymentScheduleResponse getMonthlyPayment(CreditRequest request);
    String create(CreateCreditRequest request);
    List<GetCreditInformationResponse> getCreditByUser(Long user_id);
}
