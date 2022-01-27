package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class ClaimedAuthoritiesValidationException extends AuthorizationException {

    private static final long serialVersionUID = 1L;

    public ClaimedAuthoritiesValidationException(String message) {
        super(message);
    }

}