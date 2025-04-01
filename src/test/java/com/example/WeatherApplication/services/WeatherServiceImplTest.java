package com.example.WeatherApplication.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.WeatherApplication.models.WeatherForecast;
import com.example.WeatherApplication.services.Impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

class WeatherServiceImplTest {

    @Mock
    private RestTemplate restTemplate; // Mocking API calls

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetForecast_Success() {
        // Mock API response
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        List<Map<String, Object>> periods = new ArrayList<>();
        Map<String, Object> periodData = new HashMap<>();

        periodData.put("name", "Monday");
        periodData.put("temperature", 25);
        periodData.put("shortForecast", "Sunny");
        periods.add(periodData);
        properties.put("periods", periods);
        mockResponse.put("properties", properties);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        // Test method
        WeatherForecast result = weatherService.getForecast(37.7749, -122.4194, LocalDate.now(),false);

        // Assertions
        assertNotNull(result);
        assertEquals(1, periodData.size());
        assertEquals("Monday", periodData.get("name"));
        assertEquals(25, periodData.get("temperature"));
    }

    @Test
    void testGetForecast_Failure() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);
        WeatherForecast result = weatherService.getForecast(37.7749, -122.4194, LocalDate.now(),true);
        assertNull(result);
    }
}
