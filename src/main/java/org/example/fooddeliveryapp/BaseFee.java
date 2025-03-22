package org.example.fooddeliveryapp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;


@Entity
@Table(name = "base_fees")
class BaseFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String vehicleType;
    private double fee;
    public BaseFee() {}
    public BaseFee(String city, String vehicleType, double fee) {
        this.city = city;
        this.vehicleType = vehicleType;
        this.fee = fee;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
}