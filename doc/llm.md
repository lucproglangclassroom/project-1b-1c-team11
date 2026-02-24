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

---

## Session 2: Comprehensive Test Suite Development

### Tools Used
GitHub Copilot (Claude Sonnet 4.5) was used to develop comprehensive test cases for the functional implementation.

### Context and Setup
After completing the transition from mutable observer pattern (Project 1b) to immutable functional approach (Project 1c), I needed to verify the correctness of the implementation through comprehensive testing. The system uses:
- `scanLeft` for immutable state tracking
- Sliding window mechanism without mutable queues
- Functional transformations replacing observer notifications
- Five parameters controlling behavior: `minLength`, `windowSize`, `howMany`, `everyKSteps`, `minFrequency`

### How the LLM Was Used

The LLM analyzed the existing implementation and created 22 test cases organized into three categories:

**Edge Cases (9 tests):**
- Empty input handling
- Boundary conditions (input smaller/equal to window size)
- Minimum window size (size = 1)
- Parameter extremes (everyKSteps too large, minFrequency too high, howMany = 0)
- Complete filtering scenarios (minLength filters all words)
- Homogeneous input (single repeated word)

**Functional Behavior Tests (9 tests):**
- Case insensitivity verification
- Window size maintenance during sliding
- Word count decrementation as words exit the window
- Output frequency control via everyKSteps
- Dual-key sorting (frequency descendant, alphabetical ascending)
- Parameter-based filtering and limiting
- Pre-processing filters (minLength, minFrequency)

**Integration Scenarios (3 tests):**
- Multi-constraint interaction testing
- Streaming independence (emissions reflect current window only)
- Memory efficiency (words removed from map at count zero)

### Critical Implications for Functional Programming

The test suite specifically validates:

1. **Immutable State Correctness**: Tests verify that `scanLeft` properly reconstructs state at each step without mutation, particularly when decrementing counts as words exit the window.

2. **Memory Efficiency**: Validates that the `updatedWith` pattern removes map entries when counts reach zero, preventing unbounded growth despite immutability.

3. **Referential Transparency**: All tests confirm pure functional behavior—identical inputs always produce identical outputs with no hidden state.

4. **Lazy Evaluation**: Iterator-based processing ensures one-word-at-a-time computation without loading entire input into memory.

5. **State Transformation vs. Mutation**: Tests distinguish functional state reconstruction from imperative mutation, particularly in window sliding and count management.

### What Was Not Delegated

- Reviewing and understanding each test case's purpose
- Executing the test suite to verify correctness
- Analyzing test failures if any occur
- Deciding which tests to keep, modify, or extend
- Understanding the connection between tests and functional programming principles

### Learning Outcomes

This session reinforced:

- The importance of comprehensive testing for functional transformations
- How edge cases reveal assumptions about immutable data structures
- The distinction between testing mutable state changes vs. immutable state derivations
- How to verify that functional implementations maintain correctness while avoiding side effects
- The relationship between test design and understanding functional programming guarantees
- How boundary conditions interact with multiple filtering/transformation parameters

The test suite serves not just as verification but as documentation of the functional implementation's behavior and guarantees.