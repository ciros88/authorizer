package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class ClaimedPrincipalArgumentResolverException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClaimedPrincipalArgumentResolverException(String message) {
        super(message);
    }

}