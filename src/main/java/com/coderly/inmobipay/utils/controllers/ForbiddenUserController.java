package com.coderly.inmobipay.utils.controllers;

import com.coderly.inmobipay.utils.exceptions.ForbiddenException;
import com.coderly.inmobipay.utils.exceptions.NotFoundException;
import com.coderly.inmobipay.utils.models.BaseErrorResponse;
import com.coderly.inmobipay.utils.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenUserController {
    @ExceptionHandler(ForbiddenException.class)
    public BaseErrorResponse handleIdNotFound(ForbiddenException exception) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .errorCode(HttpStatus.FORBIDDEN.value())
                .build();
    }
}
