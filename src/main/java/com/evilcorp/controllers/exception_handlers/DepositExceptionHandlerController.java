package com.evilcorp.controllers.exception_handlers;

import com.evilcorp.exceptions.BankDepositNotFoundException;
import com.evilcorp.exceptions.BankNotFoundException;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.responses.DepositNotFoundResponse;
import com.evilcorp.responses.EntityAlreadyExistResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DepositExceptionHandlerController {
    @ExceptionHandler(BankDepositNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DepositNotFoundResponse handleBankDepositNotFoundException(BankNotFoundException exception) {
        return new DepositNotFoundResponse(exception.getMessage(), exception.getSearchingParam());
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EntityAlreadyExistResponse handleEntityAlreadyExistsException(EntityAlreadyExistException exception) {
        return new EntityAlreadyExistResponse(exception.getMessage(), exception.getEntity());
    }
}
