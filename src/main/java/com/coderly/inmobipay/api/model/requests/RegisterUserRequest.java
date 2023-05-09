package com.coderly.inmobipay.api.model.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    private String username;
    private String names;
    private String lastNames;
    private String email;
    private Integer age;
    private String dni;
    private String password;
}
