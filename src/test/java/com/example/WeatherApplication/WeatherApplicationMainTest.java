package com.example.WeatherApplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class WeatherApplicationMainTest {

    @Test
    void contextLoads() {
        // Test that the Spring context loads successfully
        // This implicitly tests that the application starts correctly
    }

    @Test
    void testApplicationCreation() {
        // Test that the application can be instantiated without errors
        assertDoesNotThrow(() -> {
            WeatherApplication app = new WeatherApplication();
        });
    }
}
