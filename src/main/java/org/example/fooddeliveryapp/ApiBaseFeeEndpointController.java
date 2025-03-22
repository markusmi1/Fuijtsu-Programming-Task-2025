package org.example.fooddeliveryapp;

import ch.qos.logback.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for controlling /api/basefee endpoint
 */
@RestController
@RequestMapping("/api/basefee")
class ApiBaseFeeEndpointController {
    @Autowired
    private BaseFeeService baseFeeService;

    /**
     * Shows list of base fees
     * @return base fees
     */
    @GetMapping
    public Map<String, List<BaseFee>> getAllBaseFees() {
        Map<String, List<BaseFee>> response = new HashMap<>();
        List<BaseFee> baseFees = baseFeeService.getAllBaseFees();
        response.put("Base fees", baseFees);
        return response;
    }

    /**
     * Updates base fee of requested city
     * @param body includes city, vehicleType and newFee
     * @return response of the put request
     */
    @PutMapping
    public Map<String, String> putBaseFee(@RequestBody BaseFeeUpdateRequest body) {
        Map<String, String> response = new HashMap<>();

        if(body.getCity()==null || body.getVehicleType()==null || body.getNewFee()==null){
            response.put("error", "'city', 'vehicleType' and 'newFee' must be provided in the request body");
            return response;
        }

        List<String> cities = List.of("Tallinn", "Tartu", "Pärnu");
        List<String> veichleTypes = List.of("scooter", "bike", "car");
        if (!cities.contains(body.getCity()) || !veichleTypes.contains(body.getVehicleType().toLowerCase())) {
            response.put("error", "Invalid city or vehicle type. Valid cities - (Tallinn, Tartu, Pärnu), Valid vehicle types - (Bike, Car, Scooter).");
            return response;
        }
        if (!body.getNewFee().matches("-?\\d+(\\.\\d+)?")){
            response.put("error", "newFee must be double");
            return response;
        }
        double fee = Double.parseDouble(body.getNewFee());

        baseFeeService.updateBaseFee(body.getCity(), body.getVehicleType(), fee);
        response.put("success","New fee for city " + body.getCity() + " vehicle " + body.getVehicleType() + " is " + fee);
        return response;
    }
}
