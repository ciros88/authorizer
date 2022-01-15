package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message);
    }

}
