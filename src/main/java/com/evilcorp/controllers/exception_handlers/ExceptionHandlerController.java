package com.evilcorp.controllers.exception_handlers;

import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.responses.EntityAlreadyExistResponse;
import com.evilcorp.responses.EntityNotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityNotFoundResponse> handleBankDepositNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(
                new EntityNotFoundResponse(exception.getMessage(), exception.getSearchingParam()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<EntityAlreadyExistResponse> handleEntityAlreadyExistsException(EntityAlreadyExistException exception) {
        return new ResponseEntity<>(
                new EntityAlreadyExistResponse(exception.getMessage(), exception.getEntity()),
                HttpStatus.BAD_REQUEST
        );
    }
}
