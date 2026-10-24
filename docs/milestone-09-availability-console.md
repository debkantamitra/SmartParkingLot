# Milestone 9: Live Availability And Console Interaction

## What We Built

`ParkingLotService.availableSpots()` derives free-space counts from current spot state and groups them by `SpotType`.

`Main` is now an interactive console boundary with commands for check-in, checkout, availability, and exit.

## Availability Design

The result is a snapshot returned through `Map.copyOf`. Callers can read it but cannot mutate the service's result. Every call recalculates from current spot state, so completed check-ins and checkouts appear immediately.

All spot types are initialized with zero. A full category therefore appears as `COMPACT: 0` instead of disappearing from the output.

## Responsibility Design

- `Main` reads and prints values.
- `ParkingLotService` coordinates business workflows.
- entities protect state.
- repositories store records.
- strategies select spots and calculate fees.

Exceptions are caught in `Main`, the boundary closest to the user. Domain and service classes do not print messages.

## Tradeoff

Recounting every spot is simple and correct but costs O(n) per availability request. A large production lot could maintain indexed counters, provided updates remain atomic with check-in and checkout.

## Verification

View availability, check in a car, and view it again. `COMPACT` decreases by one. Check out the ticket and confirm the count returns to its original value.
