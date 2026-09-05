package com.frodo.glamdring.infrastructure.config;

import com.frodo.glamdring.domain.domainservices.TechTrendDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

/**
 * Infrastructure configuration — wires beans that cannot be auto-detected,
 * primarily the domain service which has no Spring annotations by design.
 */
@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public TechTrendDomainService techTrendDomainService() {
        return new TechTrendDomainService();
    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
