package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class ClaimedPrincipalValidationException extends AuthorizationException {

    private static final long serialVersionUID = 1L;

    public ClaimedPrincipalValidationException(String message) {
        super(message);
    }

}