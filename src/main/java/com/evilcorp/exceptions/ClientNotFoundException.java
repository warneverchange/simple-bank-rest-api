package com.evilcorp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Setter
@Getter
public class ClientNotFoundException extends EntityNotFoundException{
    public ClientNotFoundException(Throwable cause, Object searchingParam) {
        super(cause, searchingParam);
    }

    public ClientNotFoundException(String message, Object searchingParam) {
        super(message, searchingParam);
    }

    public ClientNotFoundException(String message, Throwable cause, Object searchingParams) {
        super(message, cause, searchingParams);
    }
}
