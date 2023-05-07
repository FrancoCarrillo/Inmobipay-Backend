package com.coderly.inmobipay.infraestructure.interfaces;

import com.coderly.inmobipay.api.model.requests.CreditRequest;
import com.coderly.inmobipay.api.model.responses.CreditResponses;

import java.util.UUID;

public interface ICreditService extends CrudService<CreditRequest, CreditResponses, UUID> {
}
