package com.example.WeatherApplication.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureTest {

    @Test
    void testConstructorAndAccessors() {
        // Test data
        double high = 85.5;
        double low = 65.2;

        // Create instance
        Temperature temperature = new Temperature(high, low);

        // Verify
        assertEquals(high, temperature.high());
        assertEquals(low, temperature.low());
    }

    @Test
    void testEquality() {
        // Create two identical temperatures
        Temperature temp1 = new Temperature(75.0, 60.0);
        Temperature temp2 = new Temperature(75.0, 60.0);
        
        // Create a different temperature
        Temperature temp3 = new Temperature(75.0, 55.0);

        // Test equality
        assertEquals(temp1, temp2);
        assertNotEquals(temp1, temp3);
    }

    @Test
    void testHashCode() {
        // Create two identical temperatures
        Temperature temp1 = new Temperature(75.0, 60.0);
        Temperature temp2 = new Temperature(75.0, 60.0);
        
        // Create a different temperature
        Temperature temp3 = new Temperature(75.0, 55.0);

        // Test hash codes
        assertEquals(temp1.hashCode(), temp2.hashCode());
        assertNotEquals(temp1.hashCode(), temp3.hashCode());
    }

    @Test
    void testToString() {
        // Create temperature
        Temperature temp = new Temperature(75.0, 60.0);
        
        // Test toString
        String toString = temp.toString();
        assertTrue(toString.contains("75.0"));
        assertTrue(toString.contains("60.0"));
    }
}
