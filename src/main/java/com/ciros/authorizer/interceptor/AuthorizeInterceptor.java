package com.ciros.authorizer.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.ciros.authorizer.annotation.Authorize;
import com.ciros.authorizer.exception.AuthorizationException;
import com.ciros.authorizer.exception.AuthorizationHeaderException;
import com.ciros.authorizer.exception.ClaimedPrincipalValidationException;
import com.ciros.authorizer.exception.UnmappableAuthorizationHeaderException;
import com.ciros.authorizer.model.AuthorizationHeader;
import com.ciros.authorizer.util.AuthorizerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ciro Scognamiglio
 */

@Slf4j
@Aspect
@Component
public class AuthorizeInterceptor {

    private HttpServletRequest request;

    public AuthorizeInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Around("@annotation(com.ciros.authorizer.annotation.Authorize)")
    public Object authorize(final ProceedingJoinPoint joinPoint) throws Throwable {

        final String authorizationHeaderJson;

        try {
            authorizationHeaderJson = AuthorizerUtil.getAuthorizationHeaderFromRequest(request);
        } catch (AuthorizationHeaderException e) {
            // TODO "Missing or blank authorization header for request: %s",
            // request.getRequestURI();
            throw new AuthorizationException(String.format("%s for request: [%s][%s][%s] from [%s]", e.getMessage(),
                    request.getProtocol(), request.getMethod(), request.getRequestURL(), request.getRemoteAddr()));
        }

        log.info("Authorization header provided: {}", authorizationHeaderJson);

        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();
        String requiredPrincipal = method.getAnnotation(Authorize.class).requiredPrincipal();

        final boolean hasRequiredPrincipal = !requiredPrincipal.isEmpty();

        if (hasRequiredPrincipal && requiredPrincipal.isBlank())
            throw new AuthorizationException("Required principal is blank");

        if (hasRequiredPrincipal) {
            if (requiredPrincipal.startsWith("#")) {
                log.info("Required principal expression: {}", requiredPrincipal);
                Object requiredPrincipalArg;

                try {
                    requiredPrincipalArg = getRequiredPrincipalFromExpression(requiredPrincipal, joinPoint,
                            methodSignature);
                } catch (IllegalArgumentException e) {
                    throw new AuthorizationException("Unable to process provided expression: " + e.getMessage());
                }

                if (requiredPrincipalArg.toString().isBlank())
                    throw new AuthorizationException("Required principal is blank");

                requiredPrincipal = requiredPrincipalArg.toString();
            }
            log.info("Required principal: {}", requiredPrincipal);
        } else
            log.info("Required principal not provided");

        final String[] requiredAuthorities = method.getAnnotation(Authorize.class).requiredAuthorities();

        if (requiredAuthorities.length == 0)
            throw new AuthorizationException("Required authorities are missing");

        for (String requiredAuthority : requiredAuthorities)
            if (requiredAuthority.isBlank())
                throw new AuthorizationException("One or more required authorities are blank");

        log.info("Required authorities: {}", (Object) requiredAuthorities);

        AuthorizationHeader authorizationHeader;
        try {
            authorizationHeader = AuthorizerUtil.mapAuthorizationHeaderFromJson(authorizationHeaderJson);
        } catch (UnmappableAuthorizationHeaderException e) {
            throw new AuthorizationException(e.getMessage());
        }

        if (hasRequiredPrincipal) {

            final String principalClaimed = authorizationHeader.getClaimedPrincipal();

            try {
                AuthorizerUtil.validateClaimedPrincipal(principalClaimed);
            } catch (ClaimedPrincipalValidationException e) {
                throw new AuthorizationException("Invalid authorization header: " + e.getMessage());
            }

            log.info("Claimed principal: {}", principalClaimed);

            if (!principalClaimed.equals(requiredPrincipal))
                throw new AuthorizationException("Required principal <-> Claimed principal mismatch");

        }

        final Set<String> authoritiesClaimed = authorizationHeader.getClaimedAuthorities();

        if (authoritiesClaimed == null || authoritiesClaimed.isEmpty()
                || authoritiesClaimed.stream().anyMatch(String::isBlank))
            throw new AuthorizationException("Invalid authorization header: missing or blank claimed authorities");

        log.info("Claimed authorities: {}", authoritiesClaimed);

        final boolean matchingAllRequiredAuthorities = method.getAnnotation(Authorize.class)
                .matchingAllRequiredAuthorities();

        log.info("Authorities matching policy: {}", matchingAllRequiredAuthorities ? "match all required authorities"
                : "match any of the required authorities");

        if (matchingAllRequiredAuthorities) {
            for (String requiredAuthority : requiredAuthorities)
                if (!authoritiesClaimed.contains(requiredAuthority))
                    throw new AuthorizationException("One or more required authorities not found among those claimed");

        } else {
            boolean hasMatched = false;
            for (String requiredAuthority : requiredAuthorities) {
                if (authoritiesClaimed.contains(requiredAuthority)) {
                    hasMatched = true;
                    break;
                }
            }
            if (!hasMatched)
                throw new AuthorizationException("None of the required authorities found among those claimed");

        }

        log.info("Successful authorization");

        return joinPoint.proceed();
    }

    private Object getRequiredPrincipalFromExpression(final String expression, final ProceedingJoinPoint joinPoint,
            MethodSignature methodSignature) throws IllegalArgumentException {

        final String[] expressionAttributes = parseExpressionGetAttributes(expression);

        if (expressionAttributes.length == 0)
            throw new IllegalArgumentException("Invalid expression");

        final String[] parameterNames = methodSignature.getParameterNames();

        int parameterPosition = -1;

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(expressionAttributes[0]))
                parameterPosition = i;
        }

        if (parameterPosition == -1)
            throw new IllegalArgumentException(String.format("Param '%s' not found", expressionAttributes[0]));

        Object requiredPrincipalArg = joinPoint.getArgs()[parameterPosition];

        if (requiredPrincipalArg == null || requiredPrincipalArg.toString().isBlank())
            throw new IllegalArgumentException("Required principal is null or blank");

        if (expressionAttributes.length < 2)
            return requiredPrincipalArg;

        for (int i = 1; i < expressionAttributes.length; i++) {
            requiredPrincipalArg = getValueByGetterInvocation(requiredPrincipalArg, expressionAttributes[i]);
            if (requiredPrincipalArg == null)
                throw new IllegalArgumentException("Required principal is null");
        }
        return requiredPrincipalArg;
    }

    private Object getValueByGetterInvocation(final Object obj, final String argName) throws IllegalArgumentException {

        try {

            final Method method = obj.getClass()
                    .getMethod("get" + argName.substring(0, 1).toUpperCase() + argName.substring(1));

            return method.invoke(obj);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    String.format("Method '%s' does not exists or is not public", e.getMessage()));
        } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String[] parseExpressionGetAttributes(String expression) {

        expression = expression.substring(1);

        if (expression.endsWith("."))
            return new String[0];

        final String[] splittedExpression = expression.split("\\.");

        if (splittedExpression.length == 0)
            return new String[0];

        for (String element : splittedExpression)
            if (element.isBlank())
                return new String[0];

        return splittedExpression;
    }
}