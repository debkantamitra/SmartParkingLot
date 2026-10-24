# Milestone 4: Repository And In-Memory Storage

## What We Built

We added:

- `Repository<T>`, a generic storage contract
- `InMemoryRepository<T>`, a map-backed implementation

The same repository can store parking spots now and tickets later while retaining compile-time type safety.

## Why Add This Boundary?

Parking workflows need to save and retrieve data, but they should not contain `Map` operations or SQL. Depending on a repository interface lets us start in memory and replace the implementation with a database adapter later.

## Java Concepts

- `T` is a generic type parameter.
- `Optional<T>` represents a value that may be absent.
- `Map<String, T>` provides ID-based lookup.
- `new ArrayList<>(values.values())` returns a snapshot instead of exposing the repository's collection view.

## SOLID Connection

This applies Dependency Inversion: future parking services depend on the `Repository` abstraction rather than a concrete map or database.

It also supports Single Responsibility: the repository stores records; parking services will enforce parking workflows.

## Decision: Explicit Add And Update

Silent upsert is not appropriate for physical parking spots. A duplicated spot ID should fail visibly instead of replacing the existing record.

The repository contract will therefore expose separate operations:

- `add` succeeds only when the ID is absent.
- `update` succeeds only when the ID already exists.

This is safer and more explicit, at the cost of two operations instead of one convenient `save` method.

## Tradeoff

The repository interface adds abstraction before a real database exists. The database requirement makes that boundary useful, but repository methods must not grow into a second business-service layer.

## Verification

Store two parking spots, find one by ID, list both, remove one, and confirm it is absent.
