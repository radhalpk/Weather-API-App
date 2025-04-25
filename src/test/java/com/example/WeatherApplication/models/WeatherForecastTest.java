package com.example.WeatherApplication.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WeatherForecastTest {

    @Test
    void testCanonicalConstructor() {
        // Test data
        double latitude = 37.7749;
        double longitude = -122.4194;
        LocalDate date = LocalDate.of(2023, 5, 15);
        String forecast = "Sunny";
        Temperature temperature = new Temperature(75, 65);
        Wind wind = new Wind(10, 5, "NW");
        int pop = 20;

        // Create instance
        WeatherForecast weatherForecast = new WeatherForecast(
                latitude, longitude, date, forecast, temperature, wind, pop);

        // Verify
        assertEquals(latitude, weatherForecast.latitude());
        assertEquals(longitude, weatherForecast.longitude());
        assertEquals(date, weatherForecast.date());
        assertEquals(forecast, weatherForecast.forecast());
        assertEquals(temperature, weatherForecast.temperature());
        assertEquals(wind, weatherForecast.wind());
        assertEquals(pop, weatherForecast.pop());
    }

    @Test
    void testSecondaryConstructor() {
        // Test data
        double latitude = 40.7128;
        double longitude = -74.0060;

        // Create instance using secondary constructor
        WeatherForecast weatherForecast = new WeatherForecast(latitude, longitude);

        // Verify
        assertEquals(latitude, weatherForecast.latitude());
        assertEquals(longitude, weatherForecast.longitude());
        assertEquals(LocalDate.now().getYear(), weatherForecast.date().getYear());
        assertEquals(LocalDate.now().getMonth(), weatherForecast.date().getMonth());
        assertEquals(LocalDate.now().getDayOfMonth(), weatherForecast.date().getDayOfMonth());
        assertEquals("Default Forecast", weatherForecast.forecast());
        assertEquals(0, weatherForecast.temperature().high());
        assertEquals(0, weatherForecast.temperature().low());
        assertEquals(0, weatherForecast.wind().max());
        assertEquals(0, weatherForecast.wind().min());
        assertEquals("N", weatherForecast.wind().direction());
        assertEquals(0, weatherForecast.pop());
    }

    @Test
    void testEquality() {
        // Create two identical forecasts
        LocalDate date = LocalDate.of(2023, 6, 20);
        Temperature temp = new Temperature(80, 70);
        Wind wind = new Wind(15, 8, "SW");
        
        WeatherForecast forecast1 = new WeatherForecast(
                35.6895, 139.6917, date, "Cloudy", temp, wind, 40);
        
        WeatherForecast forecast2 = new WeatherForecast(
                35.6895, 139.6917, date, "Cloudy", temp, wind, 40);
        
        // Create a different forecast
        WeatherForecast forecast3 = new WeatherForecast(
                35.6895, 139.6917, date, "Rainy", temp, wind, 80);

        // Test equality
        assertEquals(forecast1, forecast2);
        assertNotEquals(forecast1, forecast3);
    }
}
