package com.evilcorp.controllers.exception_handlers;

import com.evilcorp.exceptions.BankNotFoundException;
import com.evilcorp.responses.BankNotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BankExceptionHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BankNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BankNotFoundResponse handleBankNotFoundException(BankNotFoundException exception) {
        return new BankNotFoundResponse(exception.getMessage(), exception.getSearchingParam());
    }
}
