package org.example.fooddeliveryapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface BaseFeeRepository extends JpaRepository<BaseFee, Long> {
    Optional<BaseFee> findByCityAndVehicleType(String city, String vehicleType);
}