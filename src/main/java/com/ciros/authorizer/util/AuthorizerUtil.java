package com.ciros.authorizer.util;

import java.util.Set;

import com.ciros.authorizer.exception.AuthorizationHeaderException;
import com.ciros.authorizer.exception.ClaimedAuthoritiesValidationException;
import com.ciros.authorizer.exception.ClaimedPrincipalValidationException;
import com.ciros.authorizer.exception.UnmappableAuthorizationHeaderException;
import com.ciros.authorizer.model.AuthorizationHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizerUtil {

    public static AuthorizationHeader mapAuthorizationHeader(final String authorizationHeader)
            throws AuthorizationHeaderException, UnmappableAuthorizationHeaderException {
        validateJsonAuthorizationHeader(authorizationHeader);
        return mapAuthorizationHeaderFromJson(authorizationHeader);
    }

    private static void validateJsonAuthorizationHeader(final String authorizationHeaderJson)
            throws AuthorizationHeaderException {

        if (authorizationHeaderJson == null || authorizationHeaderJson.isBlank())
            throw new AuthorizationHeaderException("Missing or blank authorization header");

    }

    private static AuthorizationHeader mapAuthorizationHeaderFromJson(final String authorizationHeaderJson)
            throws UnmappableAuthorizationHeaderException {

        final AuthorizationHeader authorizationHeader;
        final ObjectMapper mapper = new ObjectMapper();

        try {
            authorizationHeader = mapper.readValue(authorizationHeaderJson, AuthorizationHeader.class);
        } catch (JsonProcessingException e) {
            throw new UnmappableAuthorizationHeaderException("Unable to map authorization header: " + e.getMessage());
        }
        return authorizationHeader;
    }

    public static void validateClaimedPrincipal(final String principalClaimed)
            throws ClaimedPrincipalValidationException {

        if (principalClaimed == null || principalClaimed.isBlank())
            throw new ClaimedPrincipalValidationException("Claimed principal is missing or blank");

    }

    public static void validateClaimedAuthorities(final Set<String> authoritiesClaimed)
            throws ClaimedAuthoritiesValidationException {

        if (authoritiesClaimed == null || authoritiesClaimed.isEmpty())
            throw new ClaimedAuthoritiesValidationException("Claimed authorities are missing");

        for (String authorityClaimed : authoritiesClaimed)
            if (authorityClaimed.isBlank())
                throw new ClaimedAuthoritiesValidationException("One or more claimed authorities are blank");

    }

}
