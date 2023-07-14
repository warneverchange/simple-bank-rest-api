package com.evilcorp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
public class BankDepositNotFoundException extends EntityNotFoundException{
    public BankDepositNotFoundException(Throwable cause, Object searchingParam) {
        super(cause, searchingParam);
    }

    public BankDepositNotFoundException(String message, Object searchingParam) {
        super(message, searchingParam);
    }

    public BankDepositNotFoundException(String message, Throwable cause, Object searchingParams) {
        super(message, cause, searchingParams);
    }
}
