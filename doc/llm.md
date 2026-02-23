# LLM Usage Documentation – Project 1c

## Tools Used
I used ChatGPT to assist with refactoring and debugging during the transition from the imperative (Project 1b) version to the purely functional (Project 1c) version.

## How the LLM Was Used

The LLM assisted with:

- Refactoring the Observer-based imperative design into a purely functional design using `Iterator.scanLeft`.
- Eliminating mutable state (`var`) and mutable collections.
- Designing an immutable sliding-window state representation.
- Debugging Scala 3 package and compilation errors.
- Verifying that the implementation satisfies constant-space streaming requirements.
- Writing and improving ScalaTest unit tests for interactive behavior.
- Adding proper SIGPIPE handling to prevent JVM crashes when piping output to commands like `head`.
- Verifying sbt-native-packager configuration and execution from the command line.

## What Was Not Delegated

All architectural decisions were reviewed and implemented manually.  
The final implementation, testing, and validation of correctness were performed by me.

## Learning Outcomes

Using the LLM helped reinforce:

- The relationship between higher-order functions and stream processing.
- The difference between imperative state mutation and functional state transformation.
- How immutable data structures support scalable, streaming systems.
- How `Iterator.scanLeft` can replace Observer-style designs.

The LLM was used as a support tool, not as a replacement for understanding.