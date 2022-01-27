package com.ciros.authorizer.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ciros.authorizer.annotation.ClaimedPrincipal;
import com.ciros.authorizer.exception.AuthorizationHeaderException;
import com.ciros.authorizer.exception.ClaimedPrincipalResolverException;
import com.ciros.authorizer.exception.ClaimedPrincipalValidationException;
import com.ciros.authorizer.exception.UnmappableAuthorizationHeaderException;
import com.ciros.authorizer.model.AuthorizationHeader;
import com.ciros.authorizer.util.AuthorizerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ciro Scognamiglio
 */

@Slf4j
public class ClaimedPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(ClaimedPrincipal.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // TODO conditional logging

        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        final String authorizationHeaderJson = request.getHeader(HttpHeaders.AUTHORIZATION);
        AuthorizationHeader authorizationHeader;
        final StringBuilder stringBuilder = new StringBuilder("Claimed principal resolved:");

        try {
            authorizationHeader = AuthorizerUtil.mapAuthorizationHeader(authorizationHeaderJson);
        } catch (AuthorizationHeaderException | UnmappableAuthorizationHeaderException e) {
            throw new ClaimedPrincipalResolverException(e.getMessage());
        }
        stringBuilder.append(System.lineSeparator()).append("Authorization header provided: ")
                .append(authorizationHeaderJson);

        final String principalClaimed = authorizationHeader.getClaimedPrincipal();

        try {
            AuthorizerUtil.validateClaimedPrincipal(principalClaimed);
        } catch (ClaimedPrincipalValidationException e) {
            throw new ClaimedPrincipalResolverException(e.getMessage());
        }

        stringBuilder.append(System.lineSeparator()).append("Claimed principal: ").append(principalClaimed);

        log.debug(stringBuilder.toString());

        return principalClaimed;

    }

}
