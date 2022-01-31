package com.ciros.authorizer.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ciros.authorizer.annotation.RequiredRole;
import com.ciros.authorizer.exception.AuthorizationException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ciro Scognamiglio
 */

@Slf4j
@Aspect
@Component
public class RequiredRoleInterceptor {

    private static final String REQUIRED_ANNOTATION_TEMPLATE = "@" + RequestHeader.class.getName();

    @Around("@annotation(com.ciros.authorizer.annotation.RequiredRole)")
    public Object authorizeByRequiredRole(final ProceedingJoinPoint joinPoint) throws Throwable {

        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();

        final String requiredRole = method.getAnnotation(RequiredRole.class).value();

        if (requiredRole.isBlank())
            throw new AuthorizationException("Required role is blank");

        log.debug("Required role: {}", requiredRole);

        final String claimedRoleHeaderName = method.getAnnotation(RequiredRole.class).claimedRoleHeaderName();

        if (claimedRoleHeaderName.isBlank())
            throw new AuthorizationException("Provided claimedRoleHeaderName is blank");

        log.debug("Provided claimedRoleHeaderName: {}", claimedRoleHeaderName);

        final String requiredAnnotation = REQUIRED_ANNOTATION_TEMPLATE + "(\"" + claimedRoleHeaderName + "\")";

        String[] parameterNames = methodSignature.getParameterNames();
        final Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        final Object[] args = joinPoint.getArgs();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        boolean parameterToInspectFound = false;
        String parameterName = "";
        String claimedRole = "";

        outerloop: for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (annotation instanceof RequestHeader) {
                    RequestHeader annotationToInspect = (RequestHeader) annotation;
                    if (annotationToInspect.value().equals(claimedRoleHeaderName)
                            && parameterTypes[argIndex].equals(String.class)) {
                        claimedRole = args[argIndex].toString();
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

        if (claimedRole == null || claimedRole.isBlank())
            throw new AuthorizationException("Claimed role is missing or blank");

        log.debug("Claimed role: {}", claimedRole);

        if (!claimedRole.equals(requiredRole))
            throw new AuthorizationException("Required role <-> Claimed role mismatch");

        log.info("Successful authorization");

        return joinPoint.proceed();
    }
}
