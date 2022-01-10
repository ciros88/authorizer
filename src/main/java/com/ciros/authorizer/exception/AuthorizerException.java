package com.ciros.authorizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * @author Ciro Scognamiglio
 */

public class AuthorizerException extends HttpStatusCodeException {

    private static final long serialVersionUID = 1L;

    public AuthorizerException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
