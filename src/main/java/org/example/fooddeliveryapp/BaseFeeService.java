package org.example.fooddeliveryapp;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class for updating base fee in the database
 */
@Service
class BaseFeeService {
    @Autowired
    private BaseFeeRepository repository;

    @PostConstruct
    public void initializeBaseFees() {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    new BaseFee("Tallinn", "Car", 4.0),
                    new BaseFee("Tallinn", "Scooter", 3.5),
                    new BaseFee("Tallinn", "Bike", 3.0),
                    new BaseFee("Tartu", "Car", 3.5),
                    new BaseFee("Tartu", "Scooter", 3.0),
                    new BaseFee("Tartu", "Bike", 2.5),
                    new BaseFee("Pärnu", "Car", 3.0),
                    new BaseFee("Pärnu", "Scooter", 2.5),
                    new BaseFee("Pärnu", "Bike", 2.0)
            ));
        }
    }

    public Optional<BaseFee> getBaseFee(String city, String vehicleType) {
        return repository.findByCityAndVehicleType(city, vehicleType);
    }

    public List<BaseFee> getAllBaseFees() {
        return repository.findAll();
    }

    public BaseFee updateBaseFee(String city, String vehicleType, double newFee) {
        city = city.substring(0,1).toUpperCase() + city.substring(1).toLowerCase();
        vehicleType = vehicleType.substring(0,1).toUpperCase() + vehicleType.substring(1).toLowerCase();
        System.out.println(vehicleType);
        Optional<BaseFee> baseFee = repository.findByCityAndVehicleType(city, vehicleType);
        if (baseFee.isPresent()) {
            BaseFee newBaseFee = baseFee.get();
            newBaseFee.setFee(newFee);
            return repository.save(newBaseFee);
        }
        //if in some reason base fee is not present
        return repository.save(new BaseFee(city, vehicleType, newFee));
    }
}