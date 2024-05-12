package com.app.unitech.exception.base;

import java.io.Serial;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1L;

    private final String code;
    private final String message;
    private final transient Object[] args;

    public NotFoundException(String code, String message, String... args) {
        super(code);
        this.code = code;
        this.message = message;
        this.args = args;
    }

}
