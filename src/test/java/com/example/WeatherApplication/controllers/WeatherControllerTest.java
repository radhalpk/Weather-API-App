package com.example.WeatherApplication.controllers;

import com.example.WeatherApplication.models.Temperature;
import com.example.WeatherApplication.models.WeatherForecast;
import com.example.WeatherApplication.models.Wind;
import com.example.WeatherApplication.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    private final double LAT = 37.7749;
    private final double LON = -122.4194;
    private final LocalDate TODAY = LocalDate.now();

    @Test
    void testGetForecast_DefaultParameters() throws Exception {
        // Create mock forecast
        WeatherForecast mockForecast = new WeatherForecast(
                LAT,
                LON,
                TODAY,
                "Sunny",
                new Temperature(75, 65),
                new Wind(10, 5, "NW"),
                20
        );

        // Mock service response
        when(weatherService.getForecast(eq(LAT), eq(LON), any(), eq(false)))
                .thenReturn(mockForecast);

        // Perform request and validate
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", LAT, LON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude", is(LAT)))
                .andExpect(jsonPath("$.longitude", is(LON)))
                .andExpect(jsonPath("$.forecast", is("Sunny")));

        // Verify service was called with correct parameters
        verify(weatherService).getForecast(eq(LAT), eq(LON), any(), eq(false));
    }

    @Test
    void testGetForecast_WithDate() throws Exception {
        // Create specific date
        LocalDate specificDate = LocalDate.of(2023, 7, 15);
        String dateString = specificDate.format(DateTimeFormatter.ISO_DATE);

        // Create mock forecast
        WeatherForecast mockForecast = new WeatherForecast(
                LAT,
                LON,
                specificDate,
                "Partly Cloudy",
                new Temperature(80, 70),
                new Wind(15, 8, "SW"),
                30
        );

        // Mock service response
        when(weatherService.getForecast(eq(LAT), eq(LON), eq(specificDate), eq(false)))
                .thenReturn(mockForecast);

        // Perform request and validate
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", LAT, LON)
                .param("date", dateString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(dateString))
                .andExpect(jsonPath("$.forecast", is("Partly Cloudy")));

        // Verify service was called with correct parameters
        verify(weatherService).getForecast(eq(LAT), eq(LON), eq(specificDate), eq(false));
    }

    @Test
    void testGetForecast_WithMetric() throws Exception {
        // Create mock forecast with metric values
        WeatherForecast mockForecast = new WeatherForecast(
                LAT,
                LON,
                TODAY,
                "Rainy",
                new Temperature(25, 18), // Celsius
                new Wind(16, 8, "SE"), // km/h
                60
        );

        // Mock service response
        when(weatherService.getForecast(eq(LAT), eq(LON), any(), eq(true)))
                .thenReturn(mockForecast);

        // Perform request and validate
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", LAT, LON)
                .param("metric", "true")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature.high", is(25.0)))
                .andExpect(jsonPath("$.temperature.low", is(18.0)))
                .andExpect(jsonPath("$.wind.max", is(16.0)));

        // Verify service was called with correct parameters
        verify(weatherService).getForecast(eq(LAT), eq(LON), any(), eq(true));
    }

    @Test
    void testGetForecast_WithDateAndMetric() throws Exception {
        // Create specific date
        LocalDate specificDate = LocalDate.of(2023, 8, 20);
        String dateString = specificDate.format(DateTimeFormatter.ISO_DATE);

        // Create mock forecast
        WeatherForecast mockForecast = new WeatherForecast(
                LAT,
                LON,
                specificDate,
                "Thunderstorms",
                new Temperature(28, 22), // Celsius
                new Wind(25, 12, "E"), // km/h
                80
        );

        // Mock service response
        when(weatherService.getForecast(eq(LAT), eq(LON), eq(specificDate), eq(true)))
                .thenReturn(mockForecast);

        // Perform request and validate
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", LAT, LON)
                .param("date", dateString)
                .param("metric", "true")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(dateString))
                .andExpect(jsonPath("$.forecast", is("Thunderstorms")))
                .andExpect(jsonPath("$.temperature.high", is(28.0)))
                .andExpect(jsonPath("$.pop", is(80)));

        // Verify service was called with correct parameters
        verify(weatherService).getForecast(eq(LAT), eq(LON), eq(specificDate), eq(true));
    }

    @Test
    void testGetForecast_InvalidLatitude() throws Exception {
        // Test with invalid latitude (outside -90 to 90 range)
        double invalidLat = 100.0;

        // Perform request and validate error response
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", invalidLat, LON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Latitude must be between -90 and 90 degrees")));
    }

    @Test
    void testGetForecast_InvalidLongitude() throws Exception {
        // Test with invalid longitude (outside -180 to 180 range)
        double invalidLon = 200.0;

        // Perform request and validate error response
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", LAT, invalidLon)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Longitude must be between -180 and 180 degrees")));
    }

    @Test
    void testGetForecast_ServiceError() throws Exception {
        // Mock service to throw an exception
        when(weatherService.getForecast(eq(LAT), eq(LON), any(), eq(false)))
                .thenThrow(new RuntimeException("Error fetching weather data"));

        // Perform request and validate error response
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", LAT, LON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error fetching weather data")));
    }

    @Test
    void testHandleExceptions() throws Exception {
        // Mock service to throw a generic exception
        when(weatherService.getForecast(eq(LAT), eq(LON), any(), eq(false)))
                .thenThrow(new IllegalArgumentException("Test exception"));

        // Perform request and validate error response
        mockMvc.perform(MockMvcRequestBuilders
                .get("/weather/forecast/{lat},{lon}", LAT, LON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Test exception")));
    }
}
