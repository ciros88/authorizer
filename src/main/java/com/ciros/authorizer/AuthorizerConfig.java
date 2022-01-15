package com.ciros.authorizer;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ciros.authorizer.resolver.ClaimedPrincipalArgumentResolver;

/**
 * @author Ciro Scognamiglio
 */

@Configuration
@ComponentScan("com.ciros.authorizer")
public class AuthorizerConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ClaimedPrincipalArgumentResolver());
    }

}
