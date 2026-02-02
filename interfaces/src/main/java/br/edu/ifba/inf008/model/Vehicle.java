package br.edu.ifba.inf008.model;

import java.util.HashMap;
import java.util.Map;

public class Vehicle {

    public enum VehicleType { ECONOMY, COMPACT, SUV, LUXURY, VAN, ELECTRIC }

    private String id; // Placa
    private String make;
    private String model;
    private int year;
    private String color;
    private String fuelType;
    private String transmission;
    private double mileage;
    private double dailyPrice;
    private VehicleType type;
    private Map<String, Double> additionalFees = new HashMap<>();

    public Vehicle() {}

    public Vehicle(String plate, String make, String model, int year,
                   String fuelType, String transmission, double mileage,
                   double dailyPrice, String typeStr) {
        this.id = plate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.mileage = mileage;
        this.dailyPrice = dailyPrice;
        try {
            this.type = VehicleType.valueOf(typeStr.toUpperCase());
        } catch (Exception e) {
            this.type = VehicleType.ECONOMY;
        }
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPlate() { return id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public double getMileage() { return mileage; }
    public void setMileage(double mileage) { this.mileage = mileage; }

    public double getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(double dailyPrice) { this.dailyPrice = dailyPrice; }

    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }

    public Map<String, Double> getAdditionalFees() { return additionalFees; }
    public void setAdditionalFees(Map<String, Double> additionalFees) { this.additionalFees = additionalFees; }

    @Override
    public String toString() { return make + " " + model + " (" + id + ")"; }
}