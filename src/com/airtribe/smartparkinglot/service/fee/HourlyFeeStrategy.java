package com.airtribe.smartparkinglot.service.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class HourlyFeeStrategy implements FeeCalculationStrategy {
    private final BigDecimal hourlyRate;

    public HourlyFeeStrategy(BigDecimal hourlyRate) {
        if (hourlyRate == null || hourlyRate.signum() < 0) {
            throw new IllegalArgumentException("Hourly rate must be non-negative");
        }
        this.hourlyRate = hourlyRate;
    }

    @Override
    public BigDecimal calculate(Duration duration) {
        if (duration == null || duration.isNegative()) {
            throw new IllegalArgumentException("Duration must be non-negative");
        }

        long billableHours = duration.isZero()
                ? 1
                : duration.minusNanos(1).toHours() + 1;

        return hourlyRate
                .multiply(BigDecimal.valueOf(billableHours))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
