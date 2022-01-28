package com.ciros.authorizer.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.ciros.authorizer.annotation.Authorize;
import com.ciros.authorizer.exception.AuthorizationException;
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

        final String authorizationHeaderJson = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.debug("Authorization header provided: {}", authorizationHeaderJson);

        AuthorizationHeader authorizationHeader;

        final AuthorizationHeader authorizationHeaderFromRequest = AuthorizerUtil
                .getMappedAuthorizationHeaderFromRequest(request);

        if (authorizationHeaderFromRequest != null)
            authorizationHeader = authorizationHeaderFromRequest;
        else
            authorizationHeader = AuthorizerUtil.mapAuthorizationHeader(authorizationHeaderJson);

        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();

        String requiredPrincipal = method.getAnnotation(Authorize.class).requiredPrincipal();

        final boolean hasRequiredPrincipal = !requiredPrincipal.isEmpty();

        String principalClaimed = "";

        if (hasRequiredPrincipal) {

            if (requiredPrincipal.isBlank())
                throw new AuthorizationException("Required principal is blank");

            if (requiredPrincipal.startsWith("#")) {
                log.debug("Required principal expression: {}", requiredPrincipal);

                Object requiredPrincipalFromExpression;

                try {
                    requiredPrincipalFromExpression = getRequiredPrincipalFromExpression(requiredPrincipal, joinPoint,
                            methodSignature);
                } catch (IllegalArgumentException e) {
                    throw new AuthorizationException("Unable to process provided expression: " + e.getMessage());
                }

                requiredPrincipal = requiredPrincipalFromExpression.toString();

                if (requiredPrincipal.isBlank())
                    throw new AuthorizationException("Required principal is blank");

            }

            log.debug("Required principal:\t\t{}", requiredPrincipal);

            principalClaimed = authorizationHeader.getClaimedPrincipal();

            AuthorizerUtil.validateClaimedPrincipal(principalClaimed);

            log.debug("Claimed principal:\t\t{}", principalClaimed);

        } else
            log.debug("Required principal not provided");

        final String[] requiredAuthorities = method.getAnnotation(Authorize.class).requiredAuthorities();

        if (requiredAuthorities.length == 0)
            throw new AuthorizationException("Required authorities are missing");

        for (String requiredAuthority : requiredAuthorities)
            if (requiredAuthority.isBlank())
                throw new AuthorizationException("One or more required authorities are blank");

        log.debug("Required authorities:\t{}", (Object) requiredAuthorities);

        final Set<String> claimedAuthorities = authorizationHeader.getClaimedAuthorities();

        AuthorizerUtil.validateClaimedAuthorities(claimedAuthorities);

        log.debug("Claimed authorities:\t{}", claimedAuthorities);

        final boolean matchingAllRequiredAuthorities = method.getAnnotation(Authorize.class)
                .matchingAllRequiredAuthorities();

        log.debug("Authorities matching policy: {}", matchingAllRequiredAuthorities ? "match all required authorities"
                : "just match any of the required authorities");

        if (hasRequiredPrincipal && !principalClaimed.equals(requiredPrincipal))
            throw new AuthorizationException("Required principal <-> Claimed principal mismatch");

        AuthorizerUtil.doAuthoritiesMatching(requiredAuthorities, claimedAuthorities, matchingAllRequiredAuthorities);

        log.info("Successful authorization");

        if (authorizationHeaderFromRequest == null)
            AuthorizerUtil.addMappedAuthorizationHeaderToRequest(request, authorizationHeader);

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
            throw new IllegalArgumentException("Param '" + expressionAttributes[0] + "' not found");

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
            throw new IllegalArgumentException("Method '" + e.getMessage() + "' does not exists or is not public");
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
