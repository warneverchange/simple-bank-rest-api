package com.evilcorp.controllers.exception_handlers;

import com.evilcorp.exceptions.ClientNotFoundException;
import com.evilcorp.responses.ClientNotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClientExceptionHandlerController {
    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ClientNotFoundResponse handleClientNotFoundException(ClientNotFoundException exception) {
        return new ClientNotFoundResponse(exception.getMessage(), exception.getSearchingParam());
    }
}
