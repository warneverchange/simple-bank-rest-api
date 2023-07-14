package com.evilcorp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Setter
@Getter
public class EntityAlreadyExistException extends RuntimeException {
    private Object entity;
    public EntityAlreadyExistException(String message, Object entity) {
        super(message);
        this.entity = entity;
    }

    public EntityAlreadyExistException(Throwable cause, Object entity) {
        super(cause);
        this.entity = entity;
    }

    public EntityAlreadyExistException(String message, Throwable cause, Object entity) {
        super(message, cause);
        this.entity = entity;
    }
}
