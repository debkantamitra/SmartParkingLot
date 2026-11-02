package com.airtribe.smartparkinglot.entity;

public enum SpotType {
    MOTORCYCLE(1),
    COMPACT(2),
    LARGE(3);

    private final int capacity;

    SpotType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}
