package com.airtribe.smartparkinglot.entity;

public class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License Plate cannot be blank.");
        }

        if (type == null) {
            throw new IllegalArgumentException("Vehicle Type cannot be null");
        }

        this.licensePlate = licensePlate.trim().toUpperCase();
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }
}
