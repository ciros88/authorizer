package com.ciros.authorizer.exception;

/**
 * @author Ciro Scognamiglio
 */

public class ArgumentResolverException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ArgumentResolverException(String message) {
        super(message);
    }

}