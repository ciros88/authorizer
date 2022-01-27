package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class AuthorizationHeaderException extends AuthorizationException {

    private static final long serialVersionUID = 1L;

    public AuthorizationHeaderException(String message) {
        super(message);
    }

}