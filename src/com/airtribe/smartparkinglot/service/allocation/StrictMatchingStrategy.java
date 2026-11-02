package com.airtribe.smartparkinglot.service.allocation;

import com.airtribe.smartparkinglot.entity.ParkingSpot;
import com.airtribe.smartparkinglot.entity.SpotType;
import com.airtribe.smartparkinglot.entity.Vehicle;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class StrictMatchingStrategy implements SpotAllocationStrategy {
    @Override
    public Optional<ParkingSpot> findSpot(Vehicle vehicle, Collection<ParkingSpot> spots) {
        SpotType requiredSpotType = switch (vehicle.getType()) {
            case MOTORCYCLE -> SpotType.MOTORCYCLE;
            case CAR -> SpotType.COMPACT;
            case BUS -> SpotType.LARGE;
        };

        return spots.stream()
                .filter(spot -> !spot.isOccupied())
                .filter(spot -> spot.getType() == requiredSpotType)
                .min(locationOrder());
    }

    private Comparator<ParkingSpot> locationOrder() {
        return Comparator.comparingInt(ParkingSpot::getFloorNumber)
                .thenComparingInt(ParkingSpot::getSpotNumber);
    }
}
