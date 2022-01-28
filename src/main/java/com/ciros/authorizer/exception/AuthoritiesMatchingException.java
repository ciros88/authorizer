package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class AuthoritiesMatchingException extends AuthorizationException {

    private static final long serialVersionUID = 1L;

    public AuthoritiesMatchingException(String message) {
        super(message);
    }

}