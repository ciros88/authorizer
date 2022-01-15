package com.ciros.authorizer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * <h3>Authorize HTTP requests</h3>
 * <p>
 * Any (Spring controller, service or repository) method annotated with
 * {@link Authorize @Authorize} will be successfully authorized if:
 * <ul>
 * <li>{@link org.springframework.http.HttpHeaders#AUTHORIZATION AUTHORIZATION}
 * header is present in the request as a JSON mappable to
 * {@link com.ciros.authorizer.model.AuthorizationHeader AuthorizationHeader}
 * with non-blank values and array entries
 * <li>provided {@link #requiredAuthorities() requiredAuthorities} array is not
 * empty but contains non-blank entries that match
 * {@link org.springframework.http.HttpHeaders#AUTHORIZATION AUTHORIZATION}
 * header claimed authorities. If {@link #matchingAllRequiredAuthorities()
 * matchingAllRequiredAuthorities} is true then all required authorities must
 * match claimed authorities, otherwise (default behavior) is enough matching
 * any of the required authorities
 * <li>an eventually provided {@link #requiredPrincipal() requiredPrincipal} is
 * not blank and matches the
 * {@link org.springframework.http.HttpHeaders#AUTHORIZATION AUTHORIZATION}
 * header claimed principal. If provided {@link #requiredPrincipal()
 * requiredPrincipal} starts with '#' char it will be treated as an expression.
 * Using expressions allow to get required principal from annotated method param
 * at execution time: this can be useful, for instance, for assign required
 * principal to a
 * {@link org.springframework.web.bind.annotation.PathVariable @PathVariable}
 * arg or
 * {@link org.springframework.web.bind.annotation.RequestBody @RequestBody}
 * specific DTO attribute. A syntactically valid expression is formed by
 * non-blank attributes separated by dots, it mustn't start or end with a dot
 * neither contains two or more near dots. The first expression attribute will
 * be matched with any of the annotated method params: if there is a match the
 * corresponding arg (obtained at execution time) will be used as required
 * principal; any further subsequent expression attribute will represent a
 * subclass or attribute of the obtained arg class and will accessed at
 * execution time trough public getters. The resulting required principal object
 * obtained trough an expression must be not {@code null} neither blank and must
 * matches the {@link org.springframework.http.HttpHeaders#AUTHORIZATION
 * AUTHORIZATION} header claimed principal
 * </ul>
 * <p>
 * otherwise {@link com.ciros.authorizer.exception.AuthorizationException
 * AuthorizationException} will be thrown
 * <p>
 *
 * @author Ciro Scognamiglio
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Documented
public @interface Authorize {

    String requiredPrincipal() default "";

    String[] requiredAuthorities();

    boolean matchingAllRequiredAuthorities() default false;

}
