package com.app.unitech.exception.handler;

import com.app.unitech.constants.HttpResponseConstants;
import com.app.unitech.enums.ErrorCode;
import com.app.unitech.exception.InsufficientBalanceException;
import com.app.unitech.exception.MoneyReceiverAccountException;
import com.app.unitech.exception.MoneySenderAccountException;
import com.app.unitech.exception.SameAccountException;
import com.app.unitech.exception.UserAlreadyExistException;
import com.app.unitech.exception.dto.ConstraintsViolationError;
import com.app.unitech.exception.dto.ErrorResponse;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    private static final String LOCALIZED_MESSAGE_PREFIX = "error.";
    private static final String ERROR_OCCURRED = "Occurred internal server exception : {}";
    private static final String WEB_EXCHANGE_BIND_OCCURRED = "Occurred web exchange bind exception {ex: {}}";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest webRequest, Locale locale) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        String message = getLocalizedMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), locale);
        return ofTypeForOthers(HttpStatus.INTERNAL_SERVER_ERROR, webRequest,
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleException(WebExchangeBindException ex,
                                                         WebRequest webRequest) {
        String message = Arrays.toString(ex.getDetailMessageArguments());
        log.error(WEB_EXCHANGE_BIND_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.BAD_REQUEST, webRequest, ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException ex,
                                                         WebRequest webRequest) {
        log.error(WEB_EXCHANGE_BIND_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.BAD_REQUEST, webRequest, ErrorCode.BAD_REQUEST.getCode(), ex.getMessage());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleException(
            InternalAuthenticationServiceException ex, WebRequest webRequest) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.UNAUTHORIZED, webRequest, ErrorCode.UNAUTHORIZED.getCode(), ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(
            DataIntegrityViolationException ex, WebRequest webRequest) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.NOT_ACCEPTABLE, webRequest, ErrorCode.SQL_ERROR.getCode(),
                ErrorCode.SQL_ERROR.getMessage());
    }

    @ExceptionHandler(MoneySenderAccountException.class)
    public ResponseEntity<ErrorResponse> handleException(MoneySenderAccountException ex,
                                                         WebRequest webRequest) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.BAD_REQUEST, webRequest, ErrorCode.MONEY_SENDER.getCode(),
                ErrorCode.MONEY_SENDER.getMessage());
    }

    @ExceptionHandler(MoneyReceiverAccountException.class)
    public ResponseEntity<ErrorResponse> handleException(MoneyReceiverAccountException ex,
                                                         WebRequest webRequest) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.BAD_REQUEST, webRequest, ErrorCode.MONEY_RECEIVER.getCode(),
                ErrorCode.MONEY_RECEIVER.getMessage());
    }

    @ExceptionHandler(SameAccountException.class)
    public ResponseEntity<ErrorResponse> handleException(SameAccountException ex,
                                                         WebRequest webRequest) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.BAD_REQUEST, webRequest, ErrorCode.SAME_ACCOUNT.getCode(),
                ErrorCode.SAME_ACCOUNT.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleException(UserAlreadyExistException ex,
                                                         WebRequest webRequest) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.BAD_REQUEST, webRequest, ErrorCode.USER_EXIST.getCode(),
                ErrorCode.USER_EXIST.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleException(InsufficientBalanceException ex,
                                                         WebRequest webRequest) {
        log.error(ERROR_OCCURRED, ex.getMessage(), ex);
        return ofTypeForOthers(HttpStatus.BAD_REQUEST, webRequest, ErrorCode.INSUFFICIENT_BALANCE.getCode(),
                ErrorCode.INSUFFICIENT_BALANCE.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Map<String, Object>> handle(MethodArgumentNotValidException ex, WebRequest request) {
        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
                .toList();

        return ofTypeForValidation(request, HttpStatus.BAD_REQUEST, validationErrors);
    }


    private ResponseEntity<Map<String, Object>> ofTypeForValidation(WebRequest request, HttpStatus status,
                                                                    List<ConstraintsViolationError> validationErrors) {
        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        attributes.put(HttpResponseConstants.STATUS, status.value());
        attributes.put(HttpResponseConstants.ERROR, status);
        attributes.put(HttpResponseConstants.ERRORS, validationErrors);
        attributes.put(HttpResponseConstants.PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(attributes, status);
    }

    private ResponseEntity<ErrorResponse> ofTypeForOthers(HttpStatus status, WebRequest webRequest, String code,
                                                          String message) {
        var error = ErrorResponse.builder()
                .code(code)
                .status(status.value())
                .message(message)
                .path(webRequest.getContextPath())
                .timestamp(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .build();
        return new ResponseEntity<>(error, status);
    }

    private String getLocalizedMessage(String message, Locale locale) {
        return messageSource.getMessage(LOCALIZED_MESSAGE_PREFIX + message, null, message, locale);
    }

}
