package com.ciros.authorizer.util;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ciros.authorizer.exception.AuthoritiesMatchingException;
import com.ciros.authorizer.exception.AuthorizationHeaderException;
import com.ciros.authorizer.exception.ClaimedAuthoritiesValidationException;
import com.ciros.authorizer.exception.ClaimedPrincipalValidationException;
import com.ciros.authorizer.exception.UnmappableAuthorizationHeaderException;
import com.ciros.authorizer.model.AuthorizationHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizerUtil {

    public static final String MAPPED_AUTHORIZATION_HEADER_REQUEST_ATTRIBUTE = AuthorizerUtil.class.getName()
            + ".mappedAuthorizationHeader";

    public static AuthorizationHeader getMappedAuthorizationHeaderFromRequest(final HttpServletRequest request) {
        final AuthorizationHeader mappedAuthorizationHeaderFromRequest = (AuthorizationHeader) request
                .getAttribute(AuthorizerUtil.MAPPED_AUTHORIZATION_HEADER_REQUEST_ATTRIBUTE);
        if (mappedAuthorizationHeaderFromRequest != null)
            log.debug("Mapped authorization header found in current HTTP request");
        return mappedAuthorizationHeaderFromRequest;
    }

    public static void addMappedAuthorizationHeaderToRequest(final HttpServletRequest request,
            final AuthorizationHeader mappedAuthorizationHeader) {
        request.setAttribute(MAPPED_AUTHORIZATION_HEADER_REQUEST_ATTRIBUTE, mappedAuthorizationHeader);
        log.debug("Mapped authorization header added to current HTTP request");
    }

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

    public static void doAuthoritiesMatching(final String[] requiredAuthorities, final Set<String> claimedAuthorities,
            final boolean matchingAllRequiredAuthorities) {

        if (matchingAllRequiredAuthorities) {
            for (String requiredAuthority : requiredAuthorities)
                if (!claimedAuthorities.contains(requiredAuthority))
                    throw new AuthoritiesMatchingException(
                            "One or more required authorities not found among those claimed");

        } else {
            boolean hasMatched = false;
            for (String requiredAuthority : requiredAuthorities) {
                if (claimedAuthorities.contains(requiredAuthority)) {
                    hasMatched = true;
                    break;
                }
            }
            if (!hasMatched)
                throw new AuthoritiesMatchingException("None of the required authorities found among those claimed");
        }

    }

}
