package org.example.fooddeliveryapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Class for saving data into database and getting latest weather info
 */
@Service
public class WeatherDataService {
    @Autowired
    private WeatherDataRepository repository;

    public void saveWeatherData(WeatherData data) {
        repository.save(data);
    }
    private String mapCityToStation(String city) {
        switch (city) {
            case "Tallinn": return "Tallinn-Harku";
            case "Tartu": return "Tartu-Tõravere";
            case "Pärnu": return "Pärnu";
            default: return null;
        }
    }

    public Optional<WeatherData> getLatestWeatherData(String city, LocalDateTime time) {
        String station = mapCityToStation(city);
        if (time == null)
            return station != null ? repository.findTopByStationNameOrderByTimestampDesc(station) : Optional.empty();
        return station != null ? repository.findWeatherDataByStationNameAndTimestampIsNear(station, time, 2000) : Optional.empty();
    }
}