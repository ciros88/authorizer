package com.ciros.authorizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ciro Scognamiglio
 */

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ACCESS_DENIED_MESSAGE = "Access denied: invalid or expired authorization";
    private static final String GENERIC_INTERNAL_SERVER_ERROR_MESSAGE = "An internal server error has occurred";

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> handleAuthorizationException(AuthorizationException e) {
        log.warn(e.getLocalizedMessage());
        return new ResponseEntity<Object>(ACCESS_DENIED_MESSAGE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ClaimedPrincipalArgumentResolverException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAuthorizerResolverExceptions(RuntimeException e, ServletWebRequest request) {
        log.error(e.getLocalizedMessage());
        return new ResponseEntity<Object>(GENERIC_INTERNAL_SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
