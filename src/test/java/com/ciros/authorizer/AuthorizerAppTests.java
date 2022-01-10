package com.ciros.authorizer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Ciro Scognamiglio
 */

@ActiveProfiles("test")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuthorizerTestConfig.class)
class AuthorizerAppTests {

    // TODO to resolve:
    /*
     * INFO 20012 --- [ main] .b.t.c.SpringBootTestContextBootstrapper :
     * Neither @ContextConfiguration nor @ContextHierarchy found for test class
     * [com.ciros.authorizer.AuthorizerAppTests$correctHeaderNameWrongHeaderValues],
     * using SpringBootContextLoader INFO 20012 --- [ main]
     * o.s.t.c.support.AbstractContextLoader : Could not detect default resource
     * locations for test class
     * [com.ciros.authorizer.AuthorizerAppTests$correctHeaderNameWrongHeaderValues]:
     * no resource found for suffixes {-context.xml, Context.groovy}. INFO 20012 ---
     * [ main] .b.t.c.SpringBootTestContextBootstrapper : Loaded default
     * TestExecutionListener class names from location [META-INF/spring.factories]:
     * [org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener,
     * org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener,
     * org.springframework.boot.test.autoconfigure.restdocs.
     * RestDocsTestExecutionListener,
     * org.springframework.boot.test.autoconfigure.web.client.
     * MockRestServiceServerResetTestExecutionListener,
     * org.springframework.boot.test.autoconfigure.web.servlet.
     * MockMvcPrintOnlyOnFailureTestExecutionListener,
     * org.springframework.boot.test.autoconfigure.web.servlet.
     * WebDriverTestExecutionListener,
     * org.springframework.boot.test.autoconfigure.webservices.client.
     * MockWebServiceServerTestExecutionListener,
     * org.springframework.test.context.web.ServletTestExecutionListener,
     * org.springframework.test.context.support.
     * DirtiesContextBeforeModesTestExecutionListener,
     * org.springframework.test.context.event.
     * ApplicationEventsTestExecutionListener,
     * org.springframework.test.context.support.
     * DependencyInjectionTestExecutionListener,
     * org.springframework.test.context.support.DirtiesContextTestExecutionListener,
     * org.springframework.test.context.transaction.
     * TransactionalTestExecutionListener,
     * org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener,
     * org.springframework.test.context.event.EventPublishingTestExecutionListener]
     * INFO 20012 --- [ main] .b.t.c.SpringBootTestContextBootstrapper : Using
     * TestExecutionListeners:
     * [org.springframework.test.context.web.ServletTestExecutionListener@41492479,
     * org.springframework.test.context.support.
     * DirtiesContextBeforeModesTestExecutionListener@7bef7505,
     * org.springframework.test.context.event.ApplicationEventsTestExecutionListener
     * 
     * @568ef502,
     * org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener@
     * 36f05595, org.springframework.boot.test.autoconfigure.
     * SpringBootDependencyInjectionTestExecutionListener@3e12c5de,
     * org.springframework.test.context.support.DirtiesContextTestExecutionListener@
     * 3e55d844,
     * org.springframework.test.context.event.EventPublishingTestExecutionListener@
     * 1f521c69,
     * org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener@
     * 2b3abeeb, org.springframework.boot.test.autoconfigure.restdocs.
     * RestDocsTestExecutionListener@3aeb267,
     * org.springframework.boot.test.autoconfigure.web.client.
     * MockRestServiceServerResetTestExecutionListener@13a9cdae,
     * org.springframework.boot.test.autoconfigure.web.servlet.
     * MockMvcPrintOnlyOnFailureTestExecutionListener@1c972ae6,
     * org.springframework.boot.test.autoconfigure.web.servlet.
     * WebDriverTestExecutionListener@62a41279,
     * org.springframework.boot.test.autoconfigure.webservices.client.
     * MockWebServiceServerTestExecutionListener@146fa9c0]
     */

    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private WebTestClient.Builder testClient;

    @LocalServerPort
    private int port;

    @Autowired
    private ServletContext servletContext;

    @Value("${api.base-path}")
    private String apiBasePath;

    private static String BASE_URL;

    @Value("${api.endpoint.get-with-empty-authority-required-matching-any}")
    private String endpointGetWithEmptyAuthorityRequiredMatchingAny;

    @Value("${api.endpoint.get-with-empty-authority-required-matching-all}")
    private String endpointGetWithEmptyAuthorityRequiredMatchingAll;

    @Value("${api.endpoint.get-with-blank-authority-required-matching-any}")
    private String endpointGetWithBlankAuthorityRequiredMatchingAny;

    @Value("${api.endpoint.get-with-blank-authority-required-matching-all}")
    private String endpointGetWithBlankAuthorityRequiredMatchingAll;

    @Value("${api.endpoint.get-with-AUTHORITY_1-required-matching-any}")
    private String endpointGetWithAUTHORITY_1RequiredMatchingAny;

    @Value("${api.endpoint.get-with-AUTHORITY_2-required-matching-any}")
    private String endpointGetWithAUTHORITY_2RequiredMatchingAny;

    @Value("${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-any}")
    private String endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAny;

    @Value("${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-any}")
    private String endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAny;

    @Value("${api.endpoint.get-with-AUTHORITY_1-required-matching-all}")
    private String endpointGetWithAUTHORITY_1RequiredMatchingAll;

    @Value("${api.endpoint.get-with-AUTHORITY_2-required-matching-all}")
    private String endpointGetWithAUTHORITY_2RequiredMatchingAll;

    @Value("${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-all}")
    private String endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAll;

    @Value("${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-all}")
    private String endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAll;

    @Value("${api.endpoint.get-with-AUTHORITY_1-required-matching-any-with-empty-principal-required}")
    private String endpointGetWithAUTHORITY_1RequiredMatchingAnyEmptyPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_1-required-matching-any-with-blank-principal-required}")
    private String endpointGetWithAUTHORITY_1RequiredMatchingAnyBlankPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_1-required-matching-any-with-principal-required}")
    private String endpointGetWithAUTHORITY_1RequiredMatchingAnyPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_2-required-matching-any-with-principal-required}")
    private String endpointGetWithAUTHORITY_2RequiredMatchingAnyPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-any-with-principal-required}")
    private String endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAnyPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-any-with-principal-required}")
    private String endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAnyPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_1-required-matching-all-with-principal-required}")
    private String endpointGetWithAUTHORITY_1RequiredMatchingAllPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_2-required-matching-all-with-principal-required}")
    private String endpointGetWithAUTHORITY_2RequiredMatchingAllPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_1-and-AUTHORITY_2-required-matching-all-with-principal-required}")
    private String endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAllPrincipalRequired;

    @Value("${api.endpoint.get-with-AUTHORITY_2-and-AUTHORITY_1-required-matching-all-with-principal-required}")
    private String endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAllPrincipalRequired;

    private static final List<String> invalidHeaderNames = new ArrayList<String>();
    private static final List<String> invalidHeaderValues = new ArrayList<String>();

    private static final String WRONG_HEADER_NAME = "wrongHeaderName";
    private static final String CORRECT_HEADER_NAME = "authorization";

    private static final String TO_REPLACE = "toReplace";
    private static final String TO_REPLACE_1 = "toReplace1";
    private static final String TO_REPLACE_2 = "toReplace2";
    private static final String CLAIMED_ONE_AUTHORITY_TEMPLATE = "{\"claimedAuthorities\":[\"" + TO_REPLACE + "\"]}";
    private static final String CLAIMED_TWO_AUTHORITIES_TEMPLATE = "{\"claimedAuthorities\":[\"" + TO_REPLACE_1
            + "\",\"" + TO_REPLACE_2 + "\"]}";

    @Value("${authority-1}")
    private String AUTHORITY_1;

    @Value("${authority-2}")
    private String AUTHORITY_2;

    @Value("${principal}")
    private String PRINCIPAL;

    @PostConstruct
    public void postConstruct() {
        BASE_URL = String.format("http://localhost:%d%s%s", port, servletContext.getContextPath(), apiBasePath);
        webClient.baseUrl(BASE_URL);
        testClient.baseUrl(BASE_URL);
    }

    @BeforeAll
    static void setup() {

        // these should trigger error because of missing or blank authorization header
        invalidHeaderNames.add(null);
        invalidHeaderNames.add("");
        invalidHeaderNames.add(" ");
        invalidHeaderNames.add("");

        // these should trigger error because of missing or blank authorization header
        invalidHeaderValues.add(null);
        invalidHeaderValues.add("");
        invalidHeaderValues.add(" ");

        // these should trigger error because of error during JSON parse/decode/map
        invalidHeaderValues.add("dummyHeaderValue");
        invalidHeaderValues.add("{dummyHeaderValue}");
        invalidHeaderValues.add("{\"claimedAuthorities\"}");
        invalidHeaderValues.add("{\"claimedAuthorities\" }");
        invalidHeaderValues.add("{\"claimedAuthorities\":}");
        invalidHeaderValues.add("{\"claimedAuthorities\": }");
        invalidHeaderValues.add("{\"claimedAuthorities\":,\"\"}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[invalidAuthority]}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[\"invalidAuthorities\",]}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[\"invalidAuthorities\",\"]}");

        // these should trigger error because of missing or blank claimed authorities
        invalidHeaderValues.add("{}");
        invalidHeaderValues.add("{ }");
        invalidHeaderValues.add("{\"claimedAuthorities\":[]}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[ ]}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[\"\"]}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[\" \"]}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[\"invalidAuthorities\",\"\"]}");
        invalidHeaderValues.add("{\"claimedAuthorities\":[\"invalidAuthorities\",\" \"]}");
    }

    @BeforeEach
    void setupEach() {
    }

    @Nested
    @DisplayName("Invalid header: 403 Forbidden expected")
    class invalidHeader403ForbiddenExpected {

        @Test
        @DisplayName("No header (AUTHORITY_1 required, matching any)")
        void noHeader_AUTHORITY_1Required_MatchingAny() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny).exchange().expectStatus()
                    .isForbidden();
        }

        @Test
        @DisplayName("No header (AUTHORITY_1 required, matching all)")
        void noHeader_AUTHORITY_1Required_MatchingAll() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll).exchange().expectStatus()
                    .isForbidden();
        }

        @Test
        @DisplayName("Invalid header names (AUTHORITY_1 required, matching any)")
        void invalidHeaderNames_AUTHORITY_1Required_MatchingAny() {
            for (String headerName : invalidHeaderNames)
                testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny).header(headerName)
                        .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Invalid header names (AUTHORITY_1 required, matching all)")
        void invalidHeaderNames_AUTHORITY_1Required_MatchingAll() {
            for (String headerName : invalidHeaderNames)
                testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll).header(headerName)
                        .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Wrong header names, invalid header values (AUTHORITY_1 required, matching any)")
        void wrongHeaderNamesInvalidHeaderValues_AUTHORITY_1Required_MatchingAny() {
            for (String headerValue : invalidHeaderValues)
                testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny)
                        .header(WRONG_HEADER_NAME, headerValue).exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Wrong header names, invalid header values (AUTHORITY_1 required, matching all)")
        void wrongHeaderNamesInvalidHeaderValues_AUTHORITY_1Required_MatchingAll() {
            for (String headerValue : invalidHeaderValues)
                testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll)
                        .header(WRONG_HEADER_NAME, headerValue).exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Wrong header names, correct header values (AUTHORITY_1 required, matching any)")
        void wrongHeaderNamesCorrectHeaderValues_AUTHORITY_1Required_MatchingAny() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny)
                    .header(WRONG_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Wrong header names, correct header values (AUTHORITY_1 required, matching all)")
        void wrongHeaderNamesCorrectHeaderValues_AUTHORITY_1Required_MatchingAll() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll)
                    .header(WRONG_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Correct header name, invalid header values (AUTHORITY_1 required, matching any)")
        void correctHeaderNameInvalidHeaderValues_AUTHORITY_1Required_MatchingAny() {
            for (String headerValue : invalidHeaderValues)
                testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny)
                        .header(CORRECT_HEADER_NAME, headerValue).exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Correct header name, invalid header values (AUTHORITY_1 required, matching all)")
        void correctHeaderNameInvalidHeaderValues_AUTHORITY_1Required_MatchingAll() {
            for (String headerValue : invalidHeaderValues)
                testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll)
                        .header(CORRECT_HEADER_NAME, headerValue).exchange().expectStatus().isForbidden();
        }

    }

    @Nested
    @DisplayName("Correct header name, wrong header values: 403 Forbidden expected")
    class correctHeaderNameWrongHeaderValues {

        @Test
        @DisplayName("AUTHORITY_1 required, matching any, AUTHORITY_2 claimed")
        void requiredAUTHORITY_1_MatchingAny_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching any, AUTHORITY_1 claimed")
        void requiredAUTHORITY_2_MatchingAny_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching all, AUTHORITY_2 claimed")
        void requiredAUTHORITY_1_MatchingAll_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching all, AUTHORITY_1 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAll_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching all, AUTHORITY_2 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAll_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching all, AUTHORITY_1 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAll_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching all, AUTHORITY_2 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAll_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2))))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching all, AUTHORITY_1 claimed")
        void requiredAUTHORITY_2_MatchingAll_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isForbidden();
        }

    }

    @Nested
    @DisplayName("Correct header, invalid annotation values: 403 Forbidden expected")
    class correctHeaderInvalidAnnotationValues {

        @Test
        @DisplayName("Empty required authority, matching any, AUTHORITY_1 claimed)")
        void emptyRequiredAuthority_MatchingAny_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithEmptyAuthorityRequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1)))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Empty required authority, matching all, AUTHORITY_1 claimed)")
        void emptyRequiredAuthority_MatchingAll_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithEmptyAuthorityRequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1)))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Blank required authority, matching any, AUTHORITY_1 claimed")
        void blankRequiredAuthority_MatchingAny_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithBlankAuthorityRequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1)))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("Blank required authority, matching all, AUTHORITY_1 claimed)")
        void blankRequiredAuthority_MatchingAll_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithBlankAuthorityRequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1)))
                    .exchange().expectStatus().isForbidden();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching any, AUTHORITY_1 claimed, blank principal required")
        void requiredAUTHORITY_1_MatchingAny_AUTHORITY_1Claimed_BlankPrincipalRequired() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAnyBlankPrincipalRequired)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1)))
                    .exchange().expectStatus().isForbidden();
        }

    }

    @Nested
    @DisplayName("Correct header name, correct header values: 200 OK expected")
    class correctHeaderNameCorrectHeaderValues {

        @Test
        @DisplayName("AUTHORITY_1 required, matching any, AUTHORITY_1 claimed")
        void requiredAUTHORITY_1_MatchingAny_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1)))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching any, AUTHORITY_1 claimed & AUTHORITY_2 claimed")
        void requiredAUTHORITY_1_MatchingAny_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2)))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching any, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_1_MatchingAny_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1)))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching any, AUTHORITY_1 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAny_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching any, AUTHORITY_1 & AUTHORITY_2 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAny_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching any, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAny_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching any, AUTHORITY_2 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAny_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching any, AUTHORITY_1 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAny_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching any, AUTHORITY_1 & AUTHORITY_2 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAny_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching any, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAny_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching any, AUTHORITY_2 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAny_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching any, AUTHORITY_1 & AUTHORITY_2 claimed")
        void requiredAUTHORITY_2_MatchingAny_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2)))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching any, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_2_MatchingAny_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME, new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1)))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching any, AUTHORITY_2 claimed")
        void requiredAUTHORITY_2_MatchingAny_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAny)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2)))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching all, AUTHORITY_1 claimed")
        void requiredAUTHORITY_1_MatchingAll_AUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching all, AUTHORITY_1 & AUTHORITY_2 claimed")
        void requiredAUTHORITY_1_MatchingAll_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching all, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_1_MatchingAll_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching all, AUTHORITY_1 & AUTHORITY_2 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAll_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 & AUTHORITY_2 required, matching all, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_1AndAUTHORITY_2_MatchingAll_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1AndAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching all, AUTHORITY_1 & AUTHORITY_2 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAll_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 & AUTHORITY_1 required, matching all, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_2AndAUTHORITY_1_MatchingAll_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2AndAUTHORITY_1RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching all, AUTHORITY_1 & AUTHORITY_2 claimed")
        void requiredAUTHORITY_2_MatchingAll_AUTHORITY_1AndAUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_1).replace(TO_REPLACE_2, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching all, AUTHORITY_2 & AUTHORITY_1 claimed")
        void requiredAUTHORITY_2_MatchingAll_AUTHORITY_2AndAUTHORITY_1Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME, (new String(CLAIMED_TWO_AUTHORITIES_TEMPLATE
                            .replace(TO_REPLACE_1, AUTHORITY_2).replace(TO_REPLACE_2, AUTHORITY_1))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_2 required, matching all, AUTHORITY_2 claimed")
        void requiredAUTHORITY_2_MatchingAll_AUTHORITY_2Claimed() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_2RequiredMatchingAll)
                    .header(CORRECT_HEADER_NAME,
                            (new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_2))))
                    .exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("AUTHORITY_1 required, matching any, AUTHORITY_1 claimed, empty principal required")
        void requiredAUTHORITY_1_MatchingAny_AUTHORITY_1Claimed_EmptyPrincipalRequired() {
            testClient.build().get().uri(endpointGetWithAUTHORITY_1RequiredMatchingAnyEmptyPrincipalRequired)
                    .header(CORRECT_HEADER_NAME,
                            new String(CLAIMED_ONE_AUTHORITY_TEMPLATE.replace(TO_REPLACE, AUTHORITY_1)))
                    .exchange().expectStatus().isOk();
        }

        // TODO principal required tests (forbidden and ok)
        // TODO test requiredPrincipal # expression
    }

}
