package com.airtribe.smartparkinglot.service.allocation;

import com.airtribe.smartparkinglot.entity.ParkingSpot;
import com.airtribe.smartparkinglot.entity.Vehicle;

import java.util.Collection;
import java.util.Optional;

public interface SpotAllocationStrategy {
    Optional<ParkingSpot> findSpot(Vehicle vehicle, Collection<ParkingSpot> spots);
}
