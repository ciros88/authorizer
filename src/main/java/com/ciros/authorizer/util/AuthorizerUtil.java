package com.ciros.authorizer.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

import com.ciros.authorizer.exception.AuthorizationHeaderException;
import com.ciros.authorizer.exception.ClaimedPrincipalValidationException;
import com.ciros.authorizer.exception.UnmappableAuthorizationHeaderException;
import com.ciros.authorizer.model.AuthorizationHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizerUtil {

    public static String getAuthorizationHeaderFromRequest(final HttpServletRequest request)
            throws IllegalArgumentException {

        final String authorizationHeaderJson = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaderJson == null || authorizationHeaderJson.isBlank())
            throw new AuthorizationHeaderException("Missing or blank authorization header");

        return authorizationHeaderJson;

    }

    public static AuthorizationHeader mapAuthorizationHeaderFromJson(final String authorizationHeaderJson) {

        final AuthorizationHeader authorizationHeader;
        final ObjectMapper mapper = new ObjectMapper();

        try {
            authorizationHeader = mapper.readValue(authorizationHeaderJson, AuthorizationHeader.class);
        } catch (JsonProcessingException e) {
            throw new UnmappableAuthorizationHeaderException("Unable to map authorization header: " + e.getMessage());
        }
        return authorizationHeader;
    }

    public static void validateClaimedPrincipal(final String principalClaimed) {

        if (principalClaimed == null || principalClaimed.isBlank())
            throw new ClaimedPrincipalValidationException("claimed principal is missing or blank");

    }

}
