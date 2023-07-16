package com.evilcorp.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
public class EntityNotFoundException extends RuntimeException{
    private Object searchingParam;

    public EntityNotFoundException(Throwable cause, Object searchingParam) {
        super(cause);
        this.searchingParam = searchingParam;
    }

    public EntityNotFoundException(String message, Object searchingParam) {
        super(message);
        this.searchingParam = searchingParam;
    }

    public EntityNotFoundException(String message, Throwable cause, Object searchingParams) {
        super(message, cause);
        this.searchingParam = searchingParams;
    }
}
