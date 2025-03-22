package org.example.fooddeliveryapp;

/**
 * Class for managing Put requests body to change base fee
 */
public class BaseFeeUpdateRequest {
    private String city;
    private String vehicleType;
    private String newFee;

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getNewFee() { return newFee; }
    public void setNewFee(String newFee) { this.newFee = newFee; }
}
