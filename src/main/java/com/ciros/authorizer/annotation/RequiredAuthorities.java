package com.ciros.authorizer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>Authorize HTTP requests</h3>
 * <p>
 * Container annotation: it allows the multiple invocation of
 * {@link RequiredAuthority @RequiredAuthority} annotation on the same method.
 * It can also be invoked directly on a method, however starting from Java
 * version 8 this would be superfluous.
 * <p>
 *
 * @author Ciro Scognamiglio
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredAuthorities {
    RequiredAuthority[] value();
}
