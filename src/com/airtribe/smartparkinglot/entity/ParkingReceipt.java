package com.airtribe.smartparkinglot.entity;

import java.time.Duration;
import java.time.Instant;
import java.math.BigDecimal;

public record ParkingReceipt(
        String ticketId,
        String licensePlate,
        String spotId,
        Instant entryTime,
        Instant exitTime,
        Duration duration,
        BigDecimal fee
) {
    public ParkingReceipt {
        if (ticketId == null || ticketId.isBlank()) {
            throw new IllegalArgumentException("Ticket ID is required");
        }
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate is required");
        }
        if (spotId == null || spotId.isBlank()) {
            throw new IllegalArgumentException("Spot ID is required");
        }
        if (entryTime == null || exitTime == null || duration == null) {
            throw new IllegalArgumentException("Receipt times are required");
        }
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Parking duration cannot be negative");
        }
        if (fee == null || fee.signum() < 0) {
            throw new IllegalArgumentException("Fee must be non-negative");
        }
    }
}
