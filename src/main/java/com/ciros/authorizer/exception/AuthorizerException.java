package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class AuthorizerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthorizerException(String message) {
        super(message);
    }

}
