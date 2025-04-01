package com.example.WeatherApplication.services.Impl;

import com.example.WeatherApplication.models.Temperature;
import com.example.WeatherApplication.models.WeatherForecast;
import com.example.WeatherApplication.models.Wind;
import com.example.WeatherApplication.services.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.base-url}")
    private String weatherApiBaseUrl;

    public WeatherServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherForecast getForecast(double lat, double lon, LocalDate date, boolean metric) {
        // Build the URL using the points endpoint to fetch forecast data
        String url = "https://api.weather.gov/points/" + lat + "," + lon;

        // Fetch the response from the Weather API
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null) {
            throw new RuntimeException("The weather data response is null.");
        }

        // Get the 'forecast' URL from the response
        Map<String, Object> properties = (Map<String, Object>) response.get("properties");
        String forecastUrl = (String) properties.get("forecast");

        if (forecastUrl == null) {
            throw new RuntimeException("Forecast URL not found in the response.");
        }

        // Fetch the forecast data using the forecast URL
        Map<String, Object> forecastResponse = restTemplate.getForObject(forecastUrl, Map.class);

        if (forecastResponse == null) {
            throw new RuntimeException("The forecast data response is null.");
        }

        // Extract the 'periods' list from the forecast data
        Map<String, Object> forecastProperties = (Map<String, Object>) forecastResponse.get("properties");
        List<Map<String, Object>> periods = (List<Map<String, Object>>) forecastProperties.get("periods");

        if (periods == null) {
            throw new RuntimeException("The forecast periods data is missing.");
        }

        StringBuilder forecastText = new StringBuilder();
        int minTemp = Integer.MAX_VALUE;
        int maxTemp = Integer.MIN_VALUE;
        int minWindSpeed = Integer.MAX_VALUE;
        int maxWindSpeed = Integer.MIN_VALUE;
        StringBuilder dayForecastText = new StringBuilder();
        StringBuilder nightForecastText = new StringBuilder();

        for (Map<String, Object> period : periods) {
            String periodName = (String) period.get("name");
            // Handle temperature field, checking if it is an Integer or String
            Object tempObject = period.get("temperature");
            String temperature = (tempObject instanceof Integer) ? String.valueOf(tempObject) : (String) tempObject;

            Object windSpeedObject = period.get("windSpeed");
            String windSpeed = (windSpeedObject instanceof Integer) ? String.valueOf(windSpeedObject) : (String) windSpeedObject;

            String shortForecast = (String) period.get("shortForecast");

            // Assuming "name" is either "Day" or "Night"
            if ("Day".equalsIgnoreCase(periodName)) {
                dayForecastText.append(shortForecast);
                minTemp = Math.min(minTemp, Integer.parseInt(temperature));
                maxTemp = Math.max(maxTemp, Integer.parseInt(temperature));
                // Parse wind speed and update min/max wind speed if necessary
                minWindSpeed = Math.min(minWindSpeed, Integer.parseInt(windSpeed));
                maxWindSpeed = Math.max(maxWindSpeed, Integer.parseInt(windSpeed));
            } else if ("Night".equalsIgnoreCase(periodName)) {
                nightForecastText.append(shortForecast);
            }
        }

        // Combine day and night forecasts
        forecastText.append("Day: ").append(dayForecastText.toString()).append(" – ");
        forecastText.append("Night: ").append(nightForecastText.toString());

        // Convert to metric units if required
        if (metric) {
            minTemp = (int) ((minTemp - 32) * 5 / 9); // Fahrenheit to Celsius
            maxTemp = (int) ((maxTemp - 32) * 5 / 9); // Fahrenheit to Celsius
            minWindSpeed = (int) (minWindSpeed * 1.60934); // MPH to KPH
            maxWindSpeed = (int) (maxWindSpeed * 1.60934); // MPH to KPH
        }

        // Return the weather forecast object
        return new WeatherForecast(lat, lon, date != null ? date : LocalDate.now(),
                forecastText.toString(), new Temperature(minTemp, maxTemp),
                new Wind(minWindSpeed, maxWindSpeed, "NW"), 80);
    }



}
