package org.example.fooddeliveryapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for controlling /api/fee endpoint
 */
@RestController
@RequestMapping("/api/fee")
public class ApiFeeEndpointController {
    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private DeliveryFeeService deliveryFeeService;

    /**
     * Checks parameters given by user and constructs response
     * @param city given city as input by user
     * @param vehicleType given vehicle type as input by user
     * @return response in JSON format for easier handling
     */
    @GetMapping
    public Map<String, Object> getDeliveryFee(@RequestParam(required = false) String city, @RequestParam(required = false) String vehicleType) {
        Map<String, Object> response = new HashMap<>();

        if (city == null || vehicleType == null) {
            response.put("error", "Both 'city' and 'vehicleType' must be provided in the request");
            return response;
        }
        List<String> cities = List.of("Tallinn", "Tartu", "Pärnu");
        List<String> veichleTypes = List.of("scooter", "bike", "car");
        if (!cities.contains(city) || !veichleTypes.contains(vehicleType.toLowerCase())) {
            response.put("error", "Invalid city or vehicle type. Valid cities - (Tallinn-Harku, Tartu-Tõravere, Pärnu), Valid vehicle types - (Bike, Car, Scooter).");
            return response;
        }

        String[] result = deliveryFeeService.calculateDeliveryFee(city, vehicleType);
        response.put(result[0], result[1]);
        return response;
    }
}
