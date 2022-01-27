package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class UnmappableAuthorizationHeaderException extends AuthorizationException {

    private static final long serialVersionUID = 1L;

    public UnmappableAuthorizationHeaderException(String message) {
        super(message);
    }

}