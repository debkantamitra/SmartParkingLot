# Milestone 5: Vehicle Check-In Orchestration

## What Are We Building?

A check-in operation will:

1. Receive a valid vehicle.
2. Ask the configured allocation strategy for a spot.
3. Occupy the selected spot.
4. Record the entry time.
5. Store and return an immutable parking ticket.

## Why Use A Service?

No single entity owns this complete workflow. A vehicle does not manage spot storage, and a parking spot should not create tickets. `ParkingLotService` will coordinate these collaborators.

This supports Single Responsibility:

- entities protect their own state
- repositories store records
- allocation strategies choose spots
- the service coordinates check-in
- `Main` handles console interaction

## Dependencies

The service will receive its repositories, allocation strategy, and `Clock` through its constructor. An injected `Clock` lets tests control the entry time without sleeps or fragile comparisons.

## Failure Policy To Decide

When no compatible spot exists, check-in cannot fulfill its command. The design must either return an empty result or throw a domain-specific exception. The selected policy will shape the service API and how the future UI handles failure.

## Decision: Domain Exception

`checkIn` returns a `ParkingTicket` when successful and throws `NoSpotAvailableException` when allocation fails. Check-in is a command, so an empty result would hide the reason it could not complete.

## Initial Implementation

`ParkingTicket` is an immutable record containing its ID, vehicle, assigned spot ID, and entry time. `ParkingLotService` receives both repositories, an allocation strategy, and a `Clock` through its constructor.

The service selects a spot, occupies it, creates the ticket, stores it, and returns it. Checkout and fee calculation remain outside this milestone.
