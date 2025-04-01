package com.example.WeatherApplication;

import com.example.WeatherApplication.controllers.WeatherController;
import com.example.WeatherApplication.models.Temperature;
import com.example.WeatherApplication.models.WeatherForecast;
import com.example.WeatherApplication.models.Wind;
import com.example.WeatherApplication.services.WeatherService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;

@SpringBootTest // Main test class to load Spring context
@AutoConfigureMockMvc
class WeatherApplicationTests {

	@Nested // Unit Tests
	@WebFluxTest(WeatherController.class) // Mocking only the controller
	class UnitTests {
		@Autowired
		private WebTestClient webTestClient;

		@MockitoBean
		private WeatherService weatherService;

		@Test
		void testGetForecast_UnitTest() {
			// Creating the mock forecast using the canonical constructor
			WeatherForecast mockForecast = new WeatherForecast(
					36.244,  // latitude
					-94.149, // longitude
					LocalDate.now(), // date (current date)
					"Sunny", // forecast
					new Temperature(85, 65), // temperature: high=85, low=65
					new Wind(10, 180, "km/h"), // wind: speed=10, direction=180 degrees, unit="km/h"
					0 // pop (probability of precipitation)
			);

			// Mocking the weatherService's getForecast method to return the mock forecast
			Mockito.when(weatherService.getForecast(36.244, -94.149, LocalDate.now(), false))
					.thenReturn(mockForecast);

			// Performing the HTTP GET request and asserting the response
			webTestClient.get()
					.uri("/weather/forecast/36.244,-94.149")
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.jsonPath("$.forecast").isEqualTo("Sunny"); // Verifying that the forecast is "Sunny"
		}

	}

	@Nested // Integration Tests
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Full Spring Boot test
	class IntegrationTests {
		@Autowired
		private WebTestClient webTestClient;

		@Test
		void testGetForecast_IntegrationTest() {
			webTestClient.get()
					.uri("/weather/forecast/36.244,-94.149")
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.jsonPath("$.forecast").exists();
		}
	}
}
