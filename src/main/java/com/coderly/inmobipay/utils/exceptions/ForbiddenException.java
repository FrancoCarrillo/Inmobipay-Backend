package com.coderly.inmobipay.utils.exceptions;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("This user doesn't have access with this endpoint");
    }
}
