package com.ciros.authorizer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>Authorize HTTP requests</h3>
 * <p>
 * Any Spring controller method annotated with
 * {@link RequiredRole @RequiredRole} will be successfully authorized if:
 * <ul>
 * <li>{@link #value() value} is not blank: it represents the required role
 * <li>{@link #claimedRoleHeaderName() claimedRoleHeaderName} (default value:
 * "Role") is not blank too
 * <li>an header with name {@link #claimedRoleHeaderName()
 * claimedRoleHeaderName} is present in the request with a non-blank value
 * representing the claimed role
 * <li>a method parameter of type {@link java.lang.String String} annotated with
 * {@link org.springframework.web.bind.annotation.RequestHeader @RequestHeader}
 * is present, where the
 * {@link org.springframework.web.bind.annotation.RequestHeader#name()
 * RequestHeader#name()} is {@link #claimedRoleHeaderName()
 * claimedRoleHeaderName} and its argument value (obtained at runtime) is not
 * blank: it represents the claimed role
 * <li>the claimed role matches the required role
 * </ul>
 * <p>
 * otherwise an exception of type
 * {@link com.ciros.authorizer.exception.AuthorizationException
 * AuthorizationException} (or a subclass of it) will be thrown
 * <p>
 *
 * @author Ciro Scognamiglio
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredRole {

    String value();

    String claimedRoleHeaderName() default "Role";

}
