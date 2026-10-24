# Agent Instructions

SmartParkingLot is a step-by-step Java learning project. Completing the application and explaining why its design works are equally important.

## Teaching Flow For Every Meaningful Change

Explain:

1. The parking problem being solved.
2. The Java concept involved.
3. The design choice being made.
4. The relevant SOLID principle, when applicable.
5. What would go wrong without the approach.
6. The design cost or tradeoff.
7. The smallest useful implementation.
8. How to verify the behavior.
9. A 5-15 minute exercise or reflection.

## Development Style

- Prefer small, working milestones over large jumps.
- Keep examples tied to vehicles, spots, tickets, fees, availability, or concurrency.
- Explain code in plain language before adding abstraction.
- Introduce patterns only when a parking requirement creates the need.
- Preserve the package base `com.airtribe.smartparkinglot`.
- Keep `docs/learning-plan.md`, milestone guides, and implemented code aligned.

## Design Principles

- Single Responsibility: entities hold state; services coordinate workflows; repositories store records; strategies vary algorithms.
- Open/Closed: new allocation and fee rules should be addable without rewriting stable workflows.
- Liskov Substitution: every strategy implementation must honor its interface contract.
- Interface Segregation: keep repository, allocation, and fee interfaces focused.
- Dependency Inversion: `ParkingLotService` depends on interfaces for storage and allocation.

## Learner Participation

The agent must implement mechanical scaffolding, including packages, enums, classes, fields, constructors, getters, imports, and routine wiring. Do not ask the learner to create boilerplate.

Learner tasks must focus on meaningful engineering work, such as:

- choosing between design alternatives and explaining the tradeoff
- defining or refining business rules
- adding validation to an existing class
- refactoring working code to improve its responsibilities
- implementing a non-trivial algorithm after discussing its behavior
- reasoning about concurrency, persistence, failure cases, or tests

Review the learner's decisions or code before moving forward. Do not leave the build broken merely to create an exercise.
