package com.ciros.authorizer.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ciro Scognamiglio
 */

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorizerException.class)
    public ResponseEntity<Object> handleAuthorizerException(AuthorizerException e) {
        log.warn(e.getLocalizedMessage());
        return new ResponseEntity<Object>(e.getLocalizedMessage(), e.getStatusCode());
    }

}
