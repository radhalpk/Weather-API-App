package com.example.WeatherApplication.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindTest {

    @Test
    void testConstructorAndAccessors() {
        // Test data
        double max = 15.5;
        double min = 8.2;
        String direction = "NE";

        // Create instance
        Wind wind = new Wind(max, min, direction);

        // Verify
        assertEquals(max, wind.max());
        assertEquals(min, wind.min());
        assertEquals(direction, wind.direction());
    }

    @Test
    void testEquality() {
        // Create two identical winds
        Wind wind1 = new Wind(20.0, 10.0, "SW");
        Wind wind2 = new Wind(20.0, 10.0, "SW");
        
        // Create a different wind
        Wind wind3 = new Wind(20.0, 10.0, "NW");

        // Test equality
        assertEquals(wind1, wind2);
        assertNotEquals(wind1, wind3);
    }

    @Test
    void testHashCode() {
        // Create two identical winds
        Wind wind1 = new Wind(20.0, 10.0, "SW");
        Wind wind2 = new Wind(20.0, 10.0, "SW");
        
        // Create a different wind
        Wind wind3 = new Wind(20.0, 10.0, "NW");

        // Test hash codes
        assertEquals(wind1.hashCode(), wind2.hashCode());
        assertNotEquals(wind1.hashCode(), wind3.hashCode());
    }

    @Test
    void testToString() {
        // Create wind
        Wind wind = new Wind(20.0, 10.0, "SW");
        
        // Test toString
        String toString = wind.toString();
        assertTrue(toString.contains("20.0"));
        assertTrue(toString.contains("10.0"));
        assertTrue(toString.contains("SW"));
    }
}
