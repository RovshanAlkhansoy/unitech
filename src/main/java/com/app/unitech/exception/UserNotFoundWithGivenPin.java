package com.app.unitech.exception;

import com.app.unitech.enums.ErrorCode;
import com.app.unitech.exception.base.NotFoundException;

public class UserNotFoundWithGivenPin extends NotFoundException {

    public UserNotFoundWithGivenPin(String... args) {
        super(ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getMessage(), args);
    }

}
