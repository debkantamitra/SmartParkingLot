# Milestone 1: Java Setup And First Run

## What And Why

Create `Main` in `com.airtribe.smartparkinglot` and print a welcome message. Packaging prevents the default package from becoming a dead end as the system grows.

## Concepts And Design

`javac` compiles source into JVM bytecode; `java` runs the named packaged class. This milestone uses no design pattern because there is no varying behavior yet.

## Without This Design

Keeping every class in the default package makes imports and boundaries unclear. The tradeoff is slightly longer folder and class names.

## Verify

```bash
javac -d out $(find src -name '*.java')
java -cp out com.airtribe.smartparkinglot.Main
```

## Exercise

Change the welcome text, compile again, and identify where the generated `Main.class` appears under `out`.
