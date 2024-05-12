package com.app.unitech.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {


    INTERNAL_SERVER_ERROR("UNITECH-APP-0001", "Internal server error!"),
    BAD_REQUEST("UNITECH-APP-002", "Bad request!"),
    VALIDATION_ERROR("UNITECH-APP-003", "Validation error!"),
    ILLEGAL_ARGUMENT_EXCEPTION("UNITECH-APP-0004", "Illegal argument exception"),
    USER_NOT_FOUND("UNITECH-APP-0005", "User not found with given pin"),
    UNAUTHORIZED("UNITECH-APP-0006", "UNAUTHORIZED"),
    MONEY_SENDER("UNITECH-APP-0007", "Money sender account not found or Deactivated with given account number"),
    MONEY_RECEIVER("UNITECH-APP-0008", "Money receiver account not found  or Deactivated with given account number"),
    SAME_ACCOUNT("UNITECH-APP-0009", "Same account selected for transfer"),
    INSUFFICIENT_BALANCE("UNITECH-APP-0010", "Insufficient balance of sender account"),
    USER_EXIST("UNITECH-APP-0011", "User already exist with given pin"),
    SQL_ERROR("UNITECH-APP-0012", "Duplicate entry for unique constraint");


    private final String code;
    private final String message;


}
