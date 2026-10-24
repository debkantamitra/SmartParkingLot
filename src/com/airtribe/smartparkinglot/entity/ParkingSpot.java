package com.airtribe.smartparkinglot.entity;

public class ParkingSpot {
    private final String id;
    private final int floorNumber;
    private final int spotNumber;
    private final SpotType type;
    private boolean occupied;

    public ParkingSpot(String id, int floorNumber, int spotNumber, SpotType type) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Spot ID is required");
        }
        if (floorNumber < 1) {
            throw new IllegalArgumentException("Floor number must be positive");
        }
        if (spotNumber < 1) {
            throw new IllegalArgumentException("Spot number must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Spot type is required");
        }

        this.id = id.trim().toUpperCase();
        this.floorNumber = floorNumber;
        this.spotNumber = spotNumber;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public SpotType getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy() {
        if (occupied) {
            throw new IllegalStateException("Spot is already occupied: " + id);
        }

        this.occupied = true;
    }

    public void release() {
        if (!occupied) {
            throw new IllegalStateException("Spot is already available: " + id);
        }

        this.occupied = false;
    }
}
