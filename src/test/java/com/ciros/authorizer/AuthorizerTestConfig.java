package com.ciros.authorizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Ciro Scognamiglio
 */

@Configuration
@ComponentScan("com.ciros.authorizer")
public class AuthorizerTestConfig {

    @Bean
    WebClient.Builder webClient() {
        return WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Bean
    WebTestClient.Builder testClient() {
        return WebTestClient.bindToServer().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

}
