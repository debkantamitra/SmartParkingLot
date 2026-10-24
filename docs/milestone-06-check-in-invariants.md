# Milestone 6: Check-In Invariants And Failure Consistency

## Problem

The current service permits the same license plate to check in repeatedly. Each call can occupy another spot and create another active ticket.

## Business Invariant

One vehicle may have at most one active parking ticket. The service must check this before allocating or occupying a spot so a rejected request cannot alter availability.

## Implementation Direction

Search active tickets for a matching normalized license plate. If one exists, throw `ParkingTransactionException` containing that plate.

This validation belongs in `ParkingLotService`, not `Vehicle`, because whether a vehicle is currently parked depends on stored transactions rather than the vehicle's own fields.

## Verification

Check in one vehicle, then check in another `Vehicle` object with the same plate. The second call must throw, the ticket count must remain one, and no additional spot may become occupied.

## Failure Rollback

If ticket creation or storage fails after a spot is occupied, `checkIn` releases that spot and rethrows the original runtime exception. Returning `null` would hide the failure and give the caller no reliable outcome.
