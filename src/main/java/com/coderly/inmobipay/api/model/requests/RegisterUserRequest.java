package com.coderly.inmobipay.api.model.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterUserRequest {

    @NotEmpty
    @NotNull
    private String username;

    @NotEmpty
    @NotNull
    private String names;

    @NotEmpty
    @NotNull
    private String lastNames;

    @NotEmpty
    @NotNull
    @Email(message = "Invalid email format")
    private String email;

    @NotNull
    @Min(value = 18, message = "Age should not be less than 18")
    private Integer age;

    @Length(min = 8, max = 8, message = "DNI should be 8 characters")
    private String dni;

    @NotEmpty
    @NotNull
    @Length(min = 6, message = "Password should be at least 6 characters")
    private String password;
}
