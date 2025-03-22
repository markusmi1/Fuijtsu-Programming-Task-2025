package org.example.fooddeliveryapp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface for querying the wheather_data table
 */
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    Optional<WeatherData> findTopByStationNameOrderByTimestampDesc(String stationName);
}
