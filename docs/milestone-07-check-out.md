# Milestone 7: Checkout, Duration, And Receipts

## What We Built

Checkout now finds an active ticket, finds its assigned spot, records the exit time, calculates the duration, releases the spot, removes the active ticket, and returns an immutable receipt.

## Java Concepts

- `Instant` represents a point on the timeline.
- `Duration.between(entry, exit)` calculates elapsed time.
- A record provides an immutable receipt value.
- Repository lookups return `Optional`, which the service converts into domain failures.

## Responsibility Design

The ticket represents an active parking session. The receipt represents the completed session. Removing the active ticket after checkout also allows that license plate to check in again.

The service coordinates the workflow because no single entity owns ticket storage, time, and spot state.

## Current Boundary

The receipt does not contain a fee yet. Milestone 8 will add pricing without mixing calculation rules into checkout.

## Verification

After checkout:

- the receipt contains the original entry and new exit times
- duration is non-negative
- the spot is available
- the active ticket is absent
- the same vehicle may check in again
