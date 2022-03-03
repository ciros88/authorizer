package com.ciros.authorizer.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ciros.authorizer.annotation.RequiredAuthorities;
import com.ciros.authorizer.annotation.RequiredAuthority;
import com.ciros.authorizer.exception.AuthorizationException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ciro Scognamiglio
 */

@Slf4j
@Aspect
@Component
public class RequiredAuthorityInterceptor {

    // TODO should each interceptor be placec in its own separated class?

    // TODO search for more descriptive var names

    private static final String REQUIRED_ANNOTATION_TEMPLATE = "@" + RequestHeader.class.getName();

    @Around("@annotation(com.ciros.authorizer.annotation.RequiredAuthority)")
    public Object authorizeByRequiredAuthority(final ProceedingJoinPoint joinPoint) throws Throwable {

        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Object[] args = joinPoint.getArgs();
        final Method method = methodSignature.getMethod();
        final String requiredAuthority = method.getAnnotation(RequiredAuthority.class).value();
        final String claimedAuthorityHeaderName = method.getAnnotation(RequiredAuthority.class)
                .claimedAuthorityHeaderName();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        String[] parameterNames = methodSignature.getParameterNames();
        final Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        authorizeByRequiredAuthority(requiredAuthority, claimedAuthorityHeaderName, parameterAnnotations, args,
                parameterNames, parameterTypes);

        return joinPoint.proceed();
    }

    @Around("@annotation(com.ciros.authorizer.annotation.RequiredAuthorities)")
    public Object authorizeByRequiredAuthorities(final ProceedingJoinPoint joinPoint) throws Throwable {

        /*
         * TODO add a common validator (private method or public util?) to check against
         * invalid values before calling private authorizeByRequiredAuthority method
         */
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Object[] args = joinPoint.getArgs();
        final Method method = methodSignature.getMethod();
        RequiredAuthority[] RequiredAuthorityArray = method.getAnnotation(RequiredAuthorities.class).value();
        for (int i = 0; i < RequiredAuthorityArray.length; i++) {
            final String requiredAuthority = RequiredAuthorityArray[i].value();
            final String claimedAuthorityHeaderName = RequiredAuthorityArray[i].claimedAuthorityHeaderName();
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            String[] parameterNames = methodSignature.getParameterNames();
            final Class<?>[] parameterTypes = methodSignature.getParameterTypes();
            authorizeByRequiredAuthority(requiredAuthority, claimedAuthorityHeaderName, parameterAnnotations, args,
                    parameterNames, parameterTypes);
        }
        return joinPoint.proceed();
    }

    // TODO should be a public util?
    private void authorizeByRequiredAuthority(final String requiredAuthority, final String claimedAuthorityHeaderName,
            final Annotation[][] parameterAnnotations, final Object[] args, final String[] parameterNames,
            final Class<?>[] parameterTypes) {

        if (requiredAuthority.isBlank())
            throw new AuthorizationException("Required authority is blank");

        log.debug("Required authority: {}", requiredAuthority);

        if (claimedAuthorityHeaderName.isBlank())
            throw new AuthorizationException("Provided claimedAuthorityHeaderName is blank");

        log.debug("Provided claimedAuthorityHeaderName: {}", claimedAuthorityHeaderName);

        final String requiredAnnotation = REQUIRED_ANNOTATION_TEMPLATE + "(\"" + claimedAuthorityHeaderName + "\")";

        boolean parameterToInspectFound = false;
        String parameterName = "";
        String claimedAuthority = "";

        outerloop: for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (annotation instanceof RequestHeader) {
                    RequestHeader annotationToInspect = (RequestHeader) annotation;
                    if (annotationToInspect.value().equals(claimedAuthorityHeaderName)
                            && parameterTypes[argIndex].equals(String.class)) {
                        claimedAuthority = args[argIndex].toString();
                        parameterName = parameterNames[argIndex];
                        parameterToInspectFound = true;
                        break outerloop;
                    }
                }
            }
        }

        if (!parameterToInspectFound)
            throw new AuthorizationException(
                    "No method parameter of type String annotated with '" + requiredAnnotation + "' has been found");

        log.debug("Annotation '{}' found on type String method parameter '{}'", requiredAnnotation, parameterName);

        if (claimedAuthority == null || claimedAuthority.isBlank())
            throw new AuthorizationException("Claimed authority is missing or blank");

        log.debug("Claimed authority: {}", claimedAuthority);

        if (!claimedAuthority.equals(requiredAuthority))
            throw new AuthorizationException("Required authority <-> Claimed authority mismatch");

        log.info("Successful authorization");
    }
}
