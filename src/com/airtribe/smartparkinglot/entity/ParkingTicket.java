package com.airtribe.smartparkinglot.entity;

import java.time.Instant;

public record ParkingTicket(
        String id,
        Vehicle vehicle,
        String spotId,
        Instant entryTime
) {
    public ParkingTicket {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Ticket ID is required");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle is required");
        }
        if (spotId == null || spotId.isBlank()) {
            throw new IllegalArgumentException("Spot ID is required");
        }
        if (entryTime == null) {
            throw new IllegalArgumentException("Entry time is required");
        }
    }
}
