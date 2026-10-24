# Milestone 3: Parking-Spot Modeling And State

## What Are We Building?

We will model parking-spot size, location, and availability. By the end of this milestone, a spot will be able to protect its own occupied/available state.

We will build it in small steps:

1. Define the allowed spot types.
2. Create a parking spot with identity and location.
3. Add occupied state.
4. Add safe `occupy()` and `release()` operations.

## Why Is This Next?

We now know what kind of vehicle has arrived. Before allocating it, the system needs to describe the spaces that might hold it.

## Vehicle Type Versus Spot Type

Vehicle categories and spot categories are related, but they describe different things:

- `VehicleType.CAR` describes an incoming vehicle.
- `SpotType.COMPACT` describes a physical parking space.

Keeping them separate prevents the domain model from claiming that a parking spot is itself a vehicle type.

## First Step: SpotType

The agent creates:

```text
src/com/airtribe/smartparkinglot/entity/SpotType.java
```

Declare a public enum with exactly:

```text
MOTORCYCLE
COMPACT
LARGE
```

No sizes or compatibility logic are added yet.

`Main` temporarily prints `SpotType.COMPACT` to verify it.

Mechanical class and enum scaffolding is agent-owned. Learner work begins where a business rule or design tradeoff must be decided.

## Design Reflection

Why should a car use `VehicleType.CAR`, while its normal parking space uses `SpotType.COMPACT` rather than `SpotType.CAR`?

## Decision: Support Multiple Allocation Policies

The parking lot will support both:

- strict matching, where each vehicle uses only its corresponding spot category
- best fit, where smaller vehicles may use a larger spot when their preferred category is unavailable

The active policy can change according to operational needs.

This means compatibility must not be permanently hardcoded into `SpotType`. It is behavior that varies, so it belongs behind an allocation-strategy interface.

This design applies the Open/Closed Principle: a new policy can be added as another implementation without rewriting the stable check-in workflow. The cost is more classes and configuration than a single fixed rule.

## Implemented Strategy Design

`SpotAllocationStrategy.findSpot` receives one vehicle and the available collection of spots, then returns `Optional<ParkingSpot>`.

- `StrictMatchingStrategy` accepts only the corresponding spot category.
- `BestFitStrategy` permits sufficient capacity and chooses the smallest suitable spot.
- Both break ties by floor number and then spot number.
- A strategy only selects a spot. It does not mutate the spot or perform check-in.

Keeping selection separate from mutation gives the strategy one responsibility and allows the future parking service to control the complete check-in transaction.

## Completed State Rules

`ParkingSpot` owns its `occupied` field and exposes controlled transitions:

- `occupy()` changes an available spot to occupied.
- `release()` changes an occupied spot to available.
- repeating either transition throws `IllegalStateException` with the spot ID.

The exception represents invalid object state rather than an invalid method argument.
