package com.airtribe.smartparkinglot.entity;

public enum VehicleType {
    MOTORCYCLE(1),
    CAR(2),
    BUS(3);

    private final int requiredSize;

    VehicleType(int requiredSize) {
        this.requiredSize = requiredSize;
    }

    public int getRequiredSize() {
        return requiredSize;
    }
}
