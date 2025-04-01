package com.example.WeatherApplication.models;

import jdk.jfr.DataAmount;

import java.time.LocalDate;



public record WeatherForecast(
        double latitude,
        double longitude,
        LocalDate date,
        String forecast,
        Temperature temperature,
        Wind wind,
        int pop
) {
    public WeatherForecast(double lat, double lon) {
        this(lat, lon, LocalDate.now(), "Default Forecast", new Temperature(0, 0), new Wind(0, 0, "N"), 0);
    }

    public void setLatitude(double v) {
    }

    public void setLongitude(double v) {
    }

    public void setForecast(String sunny) {
    }

    public void setHighTemp(int i) {
    }
}

