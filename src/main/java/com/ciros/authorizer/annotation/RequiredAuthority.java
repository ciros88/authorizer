package com.ciros.authorizer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>Authorize HTTP requests</h3>
 * <p>
 * Any Spring controller method annotated with
 * {@link RequiredAuthority @RequiredAuthority} will be successfully authorized
 * if:
 * <ul>
 * <li>{@link #value() value} is not blank: it represents the required authority
 * <li>{@link #claimedAuthorityHeaderName() claimedAuthorityHeaderName} is not
 * blank too
 * <li>an header with name {@link #claimedAuthorityHeaderName()
 * claimedAuthorityHeaderName} is present in the request with a non-blank value
 * representing the claimed authority
 * <li>a method parameter of type {@link java.lang.String String} annotated with
 * {@link org.springframework.web.bind.annotation.RequestHeader @RequestHeader}
 * is present, the
 * {@link org.springframework.web.bind.annotation.RequestHeader#name()
 * RequestHeader#name()} matches the {@link #claimedAuthorityHeaderName()
 * claimedAuthorityHeaderName} value and its argument value (obtained at
 * runtime) is not blank: it represents the claimed Authority
 * <li>the claimed authority matches the required authority
 * </ul>
 * <p>
 * otherwise an exception of type
 * {@link com.ciros.authorizer.exception.AuthorizationException
 * AuthorizationException} (or a subclass of it) will be thrown.
 * <p>
 * {@link Repeatable @Repeatable} annotation: it can me invoked multiple times
 * on the same method
 * <p>
 *
 * @author Ciro Scognamiglio
 *
 */

@Repeatable(RequiredAuthorities.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredAuthority {

    /*
     * TODO add a flag which mime matchingAllRequiredAuthorities feature
     */

    String value();

    String claimedAuthorityHeaderName();

}
