package com.coderly.inmobipay.infraestructure.interfaces;

import com.coderly.inmobipay.api.model.requests.LoginRequest;
import com.coderly.inmobipay.api.model.requests.RegisterUserRequest;
import com.coderly.inmobipay.api.model.responses.LogInResponse;

public interface ISecurityService {

    LogInResponse login(LoginRequest loginRequest);

    String register(RegisterUserRequest registerUserRequest);
}
