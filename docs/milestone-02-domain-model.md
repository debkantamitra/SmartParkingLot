# Milestone 2: Vehicle Types And Domain Modeling

## What Are We Building?

We will model the vehicles that enter the parking lot. This milestone will eventually add:

- a `VehicleType` enum
- a `Vehicle` class
- license-plate and vehicle-type fields
- constructor validation

We will add these pieces one at a time.

## Why Build This Now?

Spot allocation depends on vehicle size. Before assigning a spot, the program needs a reliable way to distinguish a motorcycle, car, and bus.

## First Java Concept: Enum

An enum represents a fixed set of allowed values. For this system, the allowed vehicle types are:

```text
MOTORCYCLE
CAR
BUS
```

Using strings would permit inconsistent values such as `"Car"`, `"car"`, and `"small car"`. An enum lets the compiler reject unsupported values.

## Design Decision

`VehicleType` will be separate from `Vehicle` because it represents a reusable category. Many vehicles can share the same type.

This supports the Single Responsibility Principle:

- `VehicleType` defines the allowed categories.
- `Vehicle` will represent one particular vehicle.

## Tradeoff

Enums work well when the allowed values are stable. Adding a completely new vehicle category requires changing and recompiling the enum.

## Your First Task

Create this file:

```text
src/com/airtribe/smartparkinglot/entity/VehicleType.java
```

Requirements:

1. Use the package `com.airtribe.smartparkinglot.entity`.
2. Declare a public enum named `VehicleType`.
3. Add exactly three values: `MOTORCYCLE`, `CAR`, and `BUS`.
4. Do not add sizes, prices, fields, constructors, or methods yet.

## How To Verify

Temporarily add this line inside `main`:

```java
System.out.println(VehicleType.CAR);
```

Add the required import, compile, and run. The last output line should be `CAR`.

## Reflection

Why is `VehicleType.CAR` safer than passing the string `"car"` throughout the application?

## Second Task: Model One Vehicle

Now create:

```text
src/com/airtribe/smartparkinglot/entity/Vehicle.java
```

Unlike the enum, this class represents one particular vehicle. Give it:

- a private final `String licensePlate`
- a private final `VehicleType type`
- a public constructor receiving both values
- `getLicensePlate()` and `getType()` methods

Do not add validation yet. We will first understand construction, encapsulation, and immutable references, then introduce validation as a separate change.

In `Main`, construct a car with plate `KA01AB1234` and print its plate and type.

## Concepts For This Task

- `private` prevents outside code from directly accessing the fields.
- `final` means each field reference can be assigned only once.
- the constructor establishes the initial object state.
- getters provide controlled read access.

This is basic encapsulation. It prevents code such as `vehicle.licensePlate = "something else"` from changing the field directly.

## Third Task: Protect Valid State

The current constructor accepts calls such as:

```java
new Vehicle("", VehicleType.CAR);
new Vehicle("KA01AB1234", null);
```

These objects compile, but they do not represent valid vehicles. Add constructor validation with these rules:

1. Reject a `null` or blank license plate using `IllegalArgumentException`.
2. Reject a `null` vehicle type using `IllegalArgumentException`.
3. Store the plate after calling `trim().toUpperCase()`.
4. Give each exception a clear message describing the invalid field.

Keep validation in the constructor so an invalid `Vehicle` can never finish construction.

## Why This Is Encapsulation

Encapsulation is more than making fields private. A class must also protect its rules whenever state enters or changes. Because the fields are final and there are no setters, the constructor is the single entry point for vehicle state.

## Verification

Confirm that a plate containing surrounding spaces is stored in normalized form. Then temporarily attempt to create a vehicle with a blank plate and observe the exception.

## Tradeoff

Strict construction makes invalid data fail early. Callers must handle or prevent the exception, but later parking logic no longer needs to repeatedly check whether every vehicle is valid.
