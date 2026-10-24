# Smart Parking Lot

A Java console application that models vehicle entry, parking-space allocation, checkout, fee calculation, and live availability for a multi-floor parking lot.

## Features

- Supports motorcycles, cars, and buses.
- Assigns the smallest suitable available spot using a best-fit strategy.
- Includes an alternative strict-matching allocation strategy.
- Prevents the same license plate from holding multiple active tickets.
- Records entry and exit times.
- Calculates fees by vehicle type and rounds each partial hour up.
- Updates availability immediately after check-in and checkout.
- Protects parking operations against concurrent access within one application instance.
- Uses in-memory repositories with explicit add and update behavior.

## Parking Rules

Vehicle and spot sizes are ranked as follows:

| Rank | Vehicle | Spot |
| ---: | --- | --- |
| 1 | Motorcycle | Motorcycle |
| 2 | Car | Compact |
| 3 | Bus | Large |

The default `BestFitStrategy` selects the smallest available spot that can hold the vehicle. Ties are resolved by the lowest floor number and then the lowest spot number.

`StrictMatchingStrategy` is also available. It restricts motorcycles to motorcycle spots, cars to compact spots, and buses to large spots.

## Fee Rules

Every started hour is billed, with a minimum charge of one hour.

| Vehicle type | Hourly rate |
| --- | ---: |
| Motorcycle | ₹20.00 |
| Car | ₹40.00 |
| Bus | ₹80.00 |

Examples for a car:

| Duration | Fee |
| --- | ---: |
| Immediate checkout | ₹40.00 |
| 60 minutes | ₹40.00 |
| 61 minutes | ₹80.00 |

Currency calculations use `BigDecimal`.

## Architecture

```text
Main (console input/output)
  └── ParkingLotService (check-in, checkout, availability)
        ├── Repository<ParkingSpot>
        ├── Repository<ParkingTicket>
        ├── SpotAllocationStrategy
        │     ├── BestFitStrategy
        │     └── StrictMatchingStrategy
        └── FeeStrategyFactory
              └── HourlyFeeStrategy
```

The main packages are:

- `entity`: vehicles, spots, tickets, receipts, and enum types
- `repository`: generic repository contract and concurrent in-memory implementation
- `service`: parking workflows
- `service.allocation`: interchangeable spot-allocation policies
- `service.fee`: fee calculation strategy and factory
- `exception`: parking-specific runtime exceptions

Tickets and receipts are immutable Java records. `ParkingSpot` owns its availability state and rejects invalid transitions such as occupying an already occupied spot.

## Concurrency

`ParkingLotService` uses a fair `ReentrantLock` around complete check-in, checkout, and availability operations. This prevents two threads from allocating the same final spot and prevents availability queries from observing a transaction halfway through.

`InMemoryRepository` uses `ConcurrentHashMap`; `putIfAbsent` and `replace` provide atomic add and update operations. Ticket numbers use `AtomicLong`.

These guarantees apply to threads sharing one `ParkingLotService` instance. Coordinating multiple application processes would require shared database transactions or another distributed coordination mechanism.

## Default Parking Lot

The console application starts with five spots:

```text
F1-M1  Floor 1  Motorcycle
F1-C1  Floor 1  Compact
F1-C2  Floor 1  Compact
F1-L1  Floor 1  Large
F2-L1  Floor 2  Large
```

## Requirements

- JDK 17 or newer
- A terminal, or IntelliJ IDEA using the included module file

## Compile And Run

From the project root:

```bash
mkdir -p out
javac -d out $(find src -name '*.java')
java -cp out com.airtribe.smartparkinglot.Main
```

The menu provides these operations:

```text
1. Check in vehicle
2. Check out vehicle
3. View availability
0. Exit
```

At check-in, enter a license plate and one of `MOTORCYCLE`, `CAR`, or `BUS`. Keep the generated ticket ID; checkout uses that ID to release the spot and calculate the receipt.

## Current Limitations

- All data is held in memory and is lost when the application exits.
- Parking spots are configured directly in `Main`.
- The console uses the best-fit allocation strategy; switching strategies currently requires changing application wiring.
- Rates are configured in `FeeStrategyFactory` rather than an external configuration source.
- Completed receipts are returned to the caller but are not stored for reporting or audit history.
- The in-process lock does not coordinate multiple deployed application instances.
