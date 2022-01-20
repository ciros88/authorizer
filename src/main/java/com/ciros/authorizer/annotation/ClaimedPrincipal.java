package com.ciros.authorizer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>Binds claimed principal to Spring controller mapping method
 * parameter</h3>
 * <p>
 * Retrieve claimed principal from an HTTP request and binds it to a Spring
 * controller mapping method parameter of type {@link java.lang.String String}
 * annotated with {@link ClaimedPrincipal @ClaimedPrincipal} if:
 * <ul>
 * <li>{@link org.springframework.http.HttpHeaders#AUTHORIZATION AUTHORIZATION}
 * header with a non-blank claimed principal is present in the request as a JSON
 * mappable to {@link com.ciros.authorizer.model.AuthorizationHeader
 * AuthorizationHeader}
 * </ul>
 * <p>
 * otherwise
 * {@link com.ciros.authorizer.exception.ClaimedPrincipalResolverException
 * ClaimedPrincipalResolverException} will be thrown
 * <p>
 *
 * @author Ciro Scognamiglio
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClaimedPrincipal {
}
