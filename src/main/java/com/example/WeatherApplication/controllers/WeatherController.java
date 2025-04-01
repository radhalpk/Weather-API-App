package com.example.WeatherApplication.controllers;
import com.example.WeatherApplication.models.WeatherForecast;
import com.example.WeatherApplication.services.WeatherService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast/{lat},{lon}")
    public WeatherForecast getForecast(
            @PathVariable double lat,
            @PathVariable double lon,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false, defaultValue = "false") boolean metric
    ) {
        return weatherService.getForecast(lat, lon, date, metric);
    }

}
