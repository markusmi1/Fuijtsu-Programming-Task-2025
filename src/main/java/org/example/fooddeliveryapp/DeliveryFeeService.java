package org.example.fooddeliveryapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Class for delivery fee calculation based on vehicle type and weather conditions
 */
@Service
public class DeliveryFeeService {
    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private BaseFeeService baseFeeService;

    /**
     * Calculates delivery fee
     * @param city given city as input by user
     * @param vehicleType given vehicle type as input by user
     * @return String[] with length 2, first element being type of response and second element being delivery fee or error message
     */
    public String[] calculateDeliveryFee(String city, String vehicleType, LocalDateTime time) {
        Optional<WeatherData> latestWeather = weatherDataService.getLatestWeatherData(city, time);
        System.out.println("Request received - City: " + city + ", Vehicle Type: " + vehicleType);
        String[] response = new String[2];

        if (latestWeather.isEmpty()) {
            response[0] = "error";
            response[1] = "No weather data available for " + city;
            return response;
        }

        double baseFee = getBaseFee(city, vehicleType);
        double extraFee = calculateExtraFee(latestWeather.get(), vehicleType);

        if (extraFee==-1){
            response[0] = "error";
            response[1] = "Usage of selected vehicle type is forbidden";
            return response;
        }
        response[0]="delivery-fee";
        response[1]=(baseFee + extraFee) + " €";
        return response;
    }

    private double getBaseFee(String city, String vehicleType) {

        switch (city) {
            case "Tallinn": return vehicleType.equalsIgnoreCase("Car") ?  baseFeeService.getBaseFee("Tallinn", "Car").get().getFee() : vehicleType.equalsIgnoreCase("Scooter") ? baseFeeService.getBaseFee("Tallinn", "Scooter").get().getFee() : baseFeeService.getBaseFee("Tallinn", "Bike").get().getFee();
            case "Tartu": return vehicleType.equalsIgnoreCase("Car") ? baseFeeService.getBaseFee("Tartu", "Car").get().getFee() : vehicleType.equalsIgnoreCase("Scooter") ? baseFeeService.getBaseFee("Tartu", "Scooter").get().getFee() : baseFeeService.getBaseFee("Tartu", "Bike").get().getFee();
            case "Pärnu": return vehicleType.equalsIgnoreCase("Car") ? baseFeeService.getBaseFee("Pärnu", "Car").get().getFee() : vehicleType.equalsIgnoreCase("Scooter") ? baseFeeService.getBaseFee("Pärnu", "Scooter").get().getFee() : baseFeeService.getBaseFee("Pärnu", "Bike").get().getFee();
            default: return 0;
        }
    }

    private double calculateExtraFee(WeatherData weatherData, String vehicleType) {
        double extraFee = 0;

        if (vehicleType.equalsIgnoreCase("scooter") || vehicleType.equalsIgnoreCase("bike")) {
            if (weatherData.getAirTemp() < -10) extraFee += 1;
            else if (weatherData.getAirTemp() < 0) extraFee += 0.5;

            if (weatherData.getWeatherPhenomenon().toLowerCase().matches(".*(snow|sleet).*")) extraFee += 1;
            if (weatherData.getWeatherPhenomenon().toLowerCase().matches(".*(rain).*")) extraFee += 0.5;

            if (weatherData.getWeatherPhenomenon().toLowerCase().matches(".*(glaze|hail|thunder).*")) {
                return -1;
            }
        }

        if (vehicleType.equalsIgnoreCase("Bike")) {
            if (weatherData.getWindSpeed() >= 10 && weatherData.getWindSpeed() <= 20) extraFee += 0.5;
            if (weatherData.getWindSpeed() > 20) {
                return -1;
            }
        }
        return extraFee;
    }
}