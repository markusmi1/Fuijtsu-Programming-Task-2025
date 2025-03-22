package org.example.fooddeliveryapp;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Interface for querying the wheather_data table
 */
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    Optional<WeatherData> findTopByStationNameOrderByTimestampDesc(String stationName);
    @Query(value = "SELECT * FROM weather_data w " +
            "WHERE w.station_name = :stationName " +
            "AND ABS(TIMESTAMPDIFF(SECOND, w.timestamp, :timestamp)) <= :threshold " +
            "ORDER BY ABS(TIMESTAMPDIFF(SECOND, w.timestamp, :timestamp)) ASC " +
            "LIMIT 1", nativeQuery = true)
    Optional<WeatherData> findWeatherDataByStationNameAndTimestampIsNear(
            @Param("stationName") String stationName,
            @Param("timestamp") LocalDateTime timestamp,
            @Param("threshold") long threshold);
    @Transactional
    void deleteWeatherDataByTimestampAfter(LocalDateTime timestamp);
}
