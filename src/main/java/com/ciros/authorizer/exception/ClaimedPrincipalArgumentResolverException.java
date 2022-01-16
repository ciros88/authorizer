package com.ciros.authorizer.exception;

import com.ciros.authorizer.annotation.ClaimedPrincipal;

/**
 * @author Ciro Scognamiglio
 */

public class ClaimedPrincipalArgumentResolverException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClaimedPrincipalArgumentResolverException(String message) {
        super("Unable to resolve argument annotated with @" + ClaimedPrincipal.class.getName() + ": " + message);
    }

}