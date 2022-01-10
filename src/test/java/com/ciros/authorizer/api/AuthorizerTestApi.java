package com.ciros.authorizer.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ciros.authorizer.annotation.Authorize;

/**
 * @author Ciro Scognamiglio
 */

@Validated
@RestController
@RequestMapping(path = "${api.base-path}")
public class AuthorizerTestApi {

    private static final String AUTHORITY_1 = "dummyAuthority1";
    private static final String AUTHORITY_2 = "dummyAuthority2";
    private static final String PRINCIPAL = "dummyPrincipal";

    @GetMapping(path = "${api.endpoint.get-with-empty-authority-required-matching-any}")
    @Authorize(requiredAuthorities = "", matchingAllRequiredAuthorities = false)
    @ResponseStatus(HttpStatus.OK)
    public void getWithEmptyAuthorityRequiredMatchingAny() {
    }

    @GetMapping(path = "${api.endpoint.get-with-empty-authority-required-matching-all}")
    @Authorize(requiredAuthorities = "", matchingAllRequiredAuthorities = true)
    @ResponseStatus(HttpStatus.OK)
    public void getWithEmptyAuthorityRequiredMatchingAll() {
    }

    @GetMapping(path = "${api.endpoint.get-with-blank-authority-required-matching-any}")
    @Authorize(requiredAuthorities = " ", matchingAllRequiredAuthorities = false)
    @ResponseStatus(HttpStatus.OK)
    public void getWithBlankAuthorityRequiredMatchingAny() {
    }

    @GetMapping(path = "${api.endpoint.get-with-blank-authority-required-matching-all}")
    @Authorize(requiredAuthorities = " ", matchingAllRequiredAuthorities = true)
    @ResponseStatus(HttpStatus.OK)
    public void getWithBlankAuthorityRequiredMatchingAll() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-required-matching-any}")
    @Authorize(requiredAuthorities = AUTHORITY_1, matchingAllRequiredAuthorities = false)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1RequiredMatchingAny() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-required-matching-any}")
    @Authorize(requiredAuthorities = AUTHORITY_2, matchingAllRequiredAuthorities = false)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2RequiredMatchingAny() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-any}")
    @Authorize(requiredAuthorities = { AUTHORITY_1, AUTHORITY_2 }, matchingAllRequiredAuthorities = false)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAny() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-any}")
    @Authorize(requiredAuthorities = { AUTHORITY_2, AUTHORITY_1 }, matchingAllRequiredAuthorities = false)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAny() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-required-matching-all}")
    @Authorize(requiredAuthorities = AUTHORITY_1, matchingAllRequiredAuthorities = true)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1RequiredMatchingAll() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-required-matching-all}")
    @Authorize(requiredAuthorities = AUTHORITY_2, matchingAllRequiredAuthorities = true)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2RequiredMatchingAll() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-all}")
    @Authorize(requiredAuthorities = { AUTHORITY_1, AUTHORITY_2 }, matchingAllRequiredAuthorities = true)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAll() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-all}")
    @Authorize(requiredAuthorities = { AUTHORITY_2, AUTHORITY_1 }, matchingAllRequiredAuthorities = true)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAll() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-required-matching-any-with-empty-principal-required}")
    @Authorize(requiredAuthorities = AUTHORITY_1, matchingAllRequiredAuthorities = false, requiredPrincipal = "")
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1RequiredMatchingAny_EmptyPrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-required-matching-any-with-blank-principal-required}")
    @Authorize(requiredAuthorities = AUTHORITY_1, matchingAllRequiredAuthorities = false, requiredPrincipal = " ")
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1RequiredMatchingAny_BlankPrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-required-matching-any-with-principal-required}")
    @Authorize(requiredAuthorities = AUTHORITY_1, matchingAllRequiredAuthorities = false, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1RequiredMatchingAny_PrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-required-matching-any-with-principal-required}")
    @Authorize(requiredAuthorities = AUTHORITY_2, matchingAllRequiredAuthorities = false, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2RequiredMatchingAny_PrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-any-with-principal-required}")
    @Authorize(requiredAuthorities = { AUTHORITY_1,
            AUTHORITY_2 }, matchingAllRequiredAuthorities = false, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAny_PrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-any-with-principal-required}")
    @Authorize(requiredAuthorities = { AUTHORITY_2,
            AUTHORITY_1 }, matchingAllRequiredAuthorities = false, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAny_PrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-required-matching-all-with-principal-required}")
    @Authorize(requiredAuthorities = AUTHORITY_1, matchingAllRequiredAuthorities = true, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1RequiredMatchingAll_PrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-required-matching-all-with-principal-required}")
    @Authorize(requiredAuthorities = AUTHORITY_2, matchingAllRequiredAuthorities = true, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2RequiredMatchingAll_PrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-all-with-principal-required}")
    @Authorize(requiredAuthorities = { AUTHORITY_1,
            AUTHORITY_2 }, matchingAllRequiredAuthorities = true, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAll_PrincipalRequired() {
    }

    @GetMapping(path = "${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-all-with-principal-required}")
    @Authorize(requiredAuthorities = { AUTHORITY_2,
            AUTHORITY_1 }, matchingAllRequiredAuthorities = true, requiredPrincipal = PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAll_PrincipalRequired() {
    }

    @GetMapping("/{dummyPrincipal}")
    @Authorize(requiredAuthorities = AUTHORITY_1, requiredPrincipal = PRINCIPAL, matchingAllRequiredAuthorities = false)
    @ResponseStatus(HttpStatus.OK)
    public void getWithAUTHORITY_1RequiredMatchingAny_WithPrincipalRequired(@PathVariable String dummyPrincipal) {
    }

    @GetMapping("/customers/{customerUuid}")
    @Authorize(requiredAuthorities = AUTHORITY_2, requiredPrincipal = "#" + PRINCIPAL)
    @ResponseStatus(HttpStatus.OK)
    public void getShoppingCartByCustomerUuid(@PathVariable String dummyPrincipal) {
    }

}
