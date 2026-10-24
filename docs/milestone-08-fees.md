# Milestone 8: Fee Strategies And Factory Creation

## Confirmed Policy

Every started hour is billed, with a minimum charge of one hour:

- motorcycle: ₹20 per hour
- car: ₹40 per hour
- bus: ₹80 per hour

Therefore, a car parked for 61 minutes costs ₹80.

## What We Built

`FeeCalculationStrategy` defines fee calculation. `HourlyFeeStrategy` implements started-hour billing with `BigDecimal`. `FeeStrategyFactory` maps a vehicle type to the configured hourly strategy.

Checkout asks the factory for the appropriate strategy and adds its result to `ParkingReceipt`.

## Why BigDecimal?

Binary floating-point values such as `double` cannot exactly represent many decimal amounts. `BigDecimal` gives deliberate decimal arithmetic and scale for currency.

## SOLID And Patterns

Strategy keeps calculation behavior separate from checkout and supports new policies such as flat, weekend, or lost-ticket fees. Factory centralizes strategy creation and rate selection.

This supports Open/Closed: a new calculation class can be added without expanding checkout into pricing conditionals.

## Started-Hour Boundary

Subtracting one nanosecond before converting to hours distinguishes an exact hour from a partial next hour:

- exactly 60 minutes → 1 hour
- 60 minutes plus any positive fraction → 2 hours

## Tradeoff

Strategy and Factory add classes around a small rule. They are justified here because pricing is explicitly expected to vary by vehicle type and may evolve independently.
