package com.ciros.authorizer.model;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Ciro Scognamiglio
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthorizationHeaderDto {

	private String claimedPrincipal;
	private Set<String> claimedAuthorities;

}
