package com.example.WeatherApplication;

import com.example.WeatherApplication.config.AppConfig;
import com.example.WeatherApplication.controllers.WeatherController;
import com.example.WeatherApplication.models.Temperature;
import com.example.WeatherApplication.models.WeatherForecast;
import com.example.WeatherApplication.models.Wind;
import com.example.WeatherApplication.services.WeatherService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		// Verify that the application context loads successfully
		assertThat(context).isNotNull();

		// Verify that key beans are available
		assertThat(context.getBean(WeatherController.class)).isNotNull();
		assertThat(context.getBean(AppConfig.class)).isNotNull();
	}

	@Nested
	class IntegrationTests {
		@Autowired
		private WebTestClient webTestClient;

		@MockBean
		private WeatherService weatherService;

		@Test
		void testGetForecast_IntegrationTest() {
			// Create test data
			double lat = 36.244;
			double lon = -94.149;
			LocalDate today = LocalDate.now();

			// Create mock forecast
			WeatherForecast mockForecast = new WeatherForecast(
					lat,
					lon,
					today,
					"Sunny with scattered clouds",
					new Temperature(85, 65),
					new Wind(10, 5, "NW"),
					30
			);

			// Mock service response
			Mockito.when(weatherService.getForecast(
					eq(lat),
					eq(lon),
					any(LocalDate.class),
					anyBoolean()
			)).thenReturn(mockForecast);

			// Test the endpoint
			webTestClient.get()
					.uri("/weather/forecast/{lat},{lon}", lat, lon)
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.jsonPath("$.latitude").isEqualTo(lat)
					.jsonPath("$.longitude").isEqualTo(lon)
					.jsonPath("$.forecast").isEqualTo("Sunny with scattered clouds")
					.jsonPath("$.temperature.high").isEqualTo(85)
					.jsonPath("$.temperature.low").isEqualTo(65)
					.jsonPath("$.wind.max").isEqualTo(10)
					.jsonPath("$.wind.min").isEqualTo(5)
					.jsonPath("$.wind.direction").isEqualTo("NW")
					.jsonPath("$.pop").isEqualTo(30);
		}

		@Test
		void testGetForecast_WithParameters() {
			// Create test data
			double lat = 40.7128;
			double lon = -74.0060;
			LocalDate specificDate = LocalDate.of(2023, 10, 15);

			// Create mock forecast
			WeatherForecast mockForecast = new WeatherForecast(
					lat,
					lon,
					specificDate,
					"Rainy",
					new Temperature(20, 15), // Celsius
					new Wind(25, 15, "SE"), // km/h
					75
			);

			// Mock service response
			Mockito.when(weatherService.getForecast(
					eq(lat),
					eq(lon),
					eq(specificDate),
					eq(true)
			)).thenReturn(mockForecast);

			// Test the endpoint with parameters
			webTestClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/weather/forecast/{lat},{lon}")
							.queryParam("date", specificDate.toString())
							.queryParam("metric", "true")
							.build(lat, lon))
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectBody()
					.jsonPath("$.date").isEqualTo(specificDate.toString())
					.jsonPath("$.forecast").isEqualTo("Rainy")
					.jsonPath("$.temperature.high").isEqualTo(20.0)
					.jsonPath("$.temperature.low").isEqualTo(15.0)
					.jsonPath("$.pop").isEqualTo(75);
		}
	}
}
