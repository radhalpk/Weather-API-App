package com.example.WeatherApplication.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    void testRestTemplateCreation() {
        // Create a RestTemplateBuilder
        RestTemplateBuilder builder = new RestTemplateBuilder();

        // Test that RestTemplate is created successfully
        RestTemplate restTemplate = appConfig.restTemplate(builder);

        // Verify the RestTemplate is not null
        assertNotNull(restTemplate);
    }
}
