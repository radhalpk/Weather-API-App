package com.example.WeatherApplication.services;

import com.example.WeatherApplication.models.WeatherForecast;

import java.time.LocalDate;

public interface WeatherService {
    WeatherForecast getForecast(double lat, double lon, LocalDate date, boolean metric);

}
