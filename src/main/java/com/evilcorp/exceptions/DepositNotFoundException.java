package com.evilcorp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Setter
@Getter
public class DepositNotFoundException extends EntityNotFoundException{
    public DepositNotFoundException(Throwable cause, Object searchingParam) {
        super(cause, searchingParam);
    }

    public DepositNotFoundException(String message, Object searchingParam) {
        super(message, searchingParam);
    }

    public DepositNotFoundException(String message, Throwable cause, Object searchingParams) {
        super(message, cause, searchingParams);
    }
}
