package com.ciros.authorizer.exception;

import com.ciros.authorizer.annotation.ClaimedPrincipal;

/**
 * @author Ciro Scognamiglio
 */

public class ClaimedPrincipalResolverException extends ArgumentResolverException {

    private static final long serialVersionUID = 1L;

    public ClaimedPrincipalResolverException(String message) {
        super("Unable to resolve argument for parameter annotated with @" + ClaimedPrincipal.class.getName() + ": "
                + message);
    }

}