package org.example.fooddeliveryapp;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Database weather_data table
 */
@Entity
@Table(name = "weather_data")
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stationName;
    private String wmoCodeOfStation;
    private double airTemp;
    private double windSpeed;
    private String weatherPhenomenon;
    private LocalDateTime timestamp;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }
    public String getWmoCodeOfStation() { return wmoCodeOfStation; }
    public void setWmoCodeOfStation(String wmoCodeOfStation) { this.wmoCodeOfStation = wmoCodeOfStation; }
    public double getAirTemp() { return airTemp; }
    public void setAirTemp(double airTemp) { this.airTemp = airTemp; }
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    public String getWeatherPhenomenon() { return weatherPhenomenon; }
    public void setWeatherPhenomenon(String weatherPhenomenon) { this.weatherPhenomenon = weatherPhenomenon; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}