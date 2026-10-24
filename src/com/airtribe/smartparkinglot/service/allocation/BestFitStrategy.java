package com.airtribe.smartparkinglot.service.allocation;

import com.airtribe.smartparkinglot.entity.ParkingSpot;
import com.airtribe.smartparkinglot.entity.Vehicle;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class BestFitStrategy implements SpotAllocationStrategy {
    @Override
    public Optional<ParkingSpot> findSpot(Vehicle vehicle, Collection<ParkingSpot> spots) {
        return spots.stream()
                .filter(spot -> !spot.isOccupied())
                .filter(spot -> spot.getType().getCapacity() >= vehicle.getType().getRequiredSize())
                .min(Comparator.comparingInt((ParkingSpot spot) -> spot.getType().getCapacity())
                        .thenComparingInt(ParkingSpot::getFloorNumber)
                        .thenComparingInt(ParkingSpot::getSpotNumber));
    }
}
