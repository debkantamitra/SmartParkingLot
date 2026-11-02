package com.airtribe.smartparkinglot.service.fee;

import java.math.BigDecimal;
import java.time.Duration;

public interface FeeCalculationStrategy {
    BigDecimal calculate(Duration duration);
}
