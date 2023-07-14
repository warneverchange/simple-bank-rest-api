package com.evilcorp.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
@Setter
public class BankNotFoundException extends EntityNotFoundException {

    public BankNotFoundException(Throwable cause, Object searchingParam) {
        super(cause, searchingParam);
    }

    public BankNotFoundException(String message, Object searchingParam) {
        super(message, searchingParam);
    }

    public BankNotFoundException(String message, Throwable cause, Object searchingParams) {
        super(message, cause, searchingParams);
    }
}