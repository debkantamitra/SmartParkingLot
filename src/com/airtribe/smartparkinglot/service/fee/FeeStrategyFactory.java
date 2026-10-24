package com.airtribe.smartparkinglot.service.fee;

import com.airtribe.smartparkinglot.entity.VehicleType;

import java.math.BigDecimal;

public final class FeeStrategyFactory {
    private FeeStrategyFactory() {
    }

    public static FeeCalculationStrategy forVehicle(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type is required");
        }

        return switch (vehicleType) {
            case MOTORCYCLE -> new HourlyFeeStrategy(new BigDecimal("20.00"));
            case CAR -> new HourlyFeeStrategy(new BigDecimal("40.00"));
            case BUS -> new HourlyFeeStrategy(new BigDecimal("80.00"));
        };
    }
}
