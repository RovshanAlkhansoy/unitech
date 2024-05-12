package com.app.unitech.exception;

import java.io.Serial;

public class SameAccountException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4328743;

    public SameAccountException() {
        super();
    }

}
