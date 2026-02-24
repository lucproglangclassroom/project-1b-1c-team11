package impl

import org.scalatest.funsuite.AnyFunSuite

class TopWordsLogicSpec extends AnyFunSuite:

  test("emits output once window is full"):
    val logic =
      new TopWordsLogic(
        minLength = 1,
        windowSize = 3,
        howMany = 2,
        everyKSteps = 1,
        minFrequency = 1
      )

    val input = Iterator("a", "b", "a", "c")

    val output =
      logic.process(input).toList

    assert(output.nonEmpty)
    assert(output.head.exists(_._1 == "a"))

  // ==================== EDGE CASES ====================

  test("empty input produces no output"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator.empty[String]
    val output = logic.process(input).toList

    assert(output.isEmpty)

  test("input smaller than window size produces no output"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 5,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("apple", "banana", "cherry")
    val output = logic.process(input).toList

    assert(output.isEmpty)

  test("input exactly equal to window size produces one output"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("apple", "banana", "cherry")
    val output = logic.process(input).toList

    assert(output.size == 1)
    assert(output.head.size == 3)

  test("window size of 1 produces output for each word"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 1,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("apple", "banana", "cherry")
    val output = logic.process(input).toList

    assert(output.size == 3)
    assert(output.head == Seq(("apple", 1)))
    assert(output(1) == Seq(("banana", 1)))
    assert(output(2) == Seq(("cherry", 1)))

  test("everyKSteps larger than input produces no output"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 100,
      minFrequency = 1
    )

    val input = Iterator("a", "b", "c", "d", "e")
    val output = logic.process(input).toList

    assert(output.isEmpty)

  test("minFrequency higher than any word count produces empty results"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 10
    )

    val input = Iterator("a", "b", "c", "d")
    val output = logic.process(input).toList

    assert(output.nonEmpty)
    assert(output.forall(_.isEmpty))

  test("howMany = 0 produces empty results"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 0,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("a", "b", "c", "d")
    val output = logic.process(input).toList

    assert(output.nonEmpty)
    assert(output.forall(_.isEmpty))

  test("minLength filters out all words"):
    val logic = new TopWordsLogic(
      minLength = 100,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("apple", "banana", "cherry", "date")
    val output = logic.process(input).toList

    assert(output.isEmpty)

  test("single word repeated fills window"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 5,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("test", "test", "test", "test", "test")
    val output = logic.process(input).toList

    assert(output.size == 1)
    assert(output.head == Seq(("test", 5)))

  // ==================== FUNCTIONAL BEHAVIOR ====================

  test("case insensitivity - uppercase and lowercase counted together"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 4,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("Apple", "APPLE", "apple", "banana")
    val output = logic.process(input).toList

    assert(output.head.exists { case (word, count) => 
      word == "apple" && count == 3 
    })

  test("window maintains correct size as it slides"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("a", "b", "c", "d", "e", "f")
    val output = logic.process(input).toList

    // Should have 4 outputs (positions 3, 4, 5, 6)
    assert(output.size == 4)
    // Each output should reflect a window of size 3
    assert(output.forall(_.map(_._2).sum <= 3))

  test("word count decreases when word exits window"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    // "a" appears twice at the start, then exits window
    val input = Iterator("a", "a", "b", "c", "d")
    val output = logic.process(input).toList

    // First output: window [a, a, b] -> a:2, b:1
    assert(output.head.find(_._1 == "a").get._2 == 2)
    // Second output: window [a, b, c] -> a:1, b:1, c:1
    assert(output(1).find(_._1 == "a").get._2 == 1)
    // Third output: window [b, c, d] -> "a" should be gone
    assert(!output(2).exists(_._1 == "a"))

  test("everyKSteps controls output frequency"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 2,
      minFrequency = 1
    )

    val input = Iterator("a", "b", "c", "d", "e", "f", "g")
    val output = logic.process(input).toList

    // Window full at step 3, but only emit at steps 4, 6
    // (steps are 1-indexed in implementation)
    assert(output.size == 2)

  test("sorting by frequency then alphabetically"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 5,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("zebra", "apple", "apple", "banana", "zebra")
    val output = logic.process(input).toList

    val result = output.head
    // apple:2, zebra:2, banana:1
    // Tied at 2, so alphabetically: apple before zebra
    assert(result.head._1 == "apple")
    assert(result(1)._1 == "zebra")
    assert(result(2)._1 == "banana")

  test("howMany limits output size"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 5,
      howMany = 2,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("a", "b", "c", "d", "e")
    val output = logic.process(input).toList

    assert(output.head.size == 2)

  test("minFrequency filters words below threshold"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 5,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 2
    )

    val input = Iterator("a", "a", "b", "c", "d")
    val output = logic.process(input).toList

    // Only "a" appears twice
    assert(output.head.size == 1)
    assert(output.head.head._1 == "a")

  test("minLength filters short words"):
    val logic = new TopWordsLogic(
      minLength = 6,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("short", "python", "medium", "javascript", "golang")
    val output = logic.process(input).toList

    // 4 words pass filter (python, medium, javascript, golang)
    // Window size 3 produces 2 emissions
    assert(output.size == 2)
    // Each emission has 3 words (window is full with 3 unique words)
    assert(output.head.size == 3)
    assert(output.head.forall(_._1.length >= 6))

  // ==================== INTEGRATION SCENARIOS ====================

  test("complex scenario with multiple constraints"):
    val logic = new TopWordsLogic(
      minLength = 4,
      windowSize = 10,
      howMany = 3,
      everyKSteps = 5,
      minFrequency = 2
    )

    val input = Iterator(
      "hello", "world", "hello", "scala", "functional",
      "programming", "hello", "scala", "immutable", "state",
      "hello", "world", "pattern"
    )
    val output = logic.process(input).toList

    // Window full at step 10, emit at step 10
    assert(output.size == 1)
    val result = output.head
    // At step 10: hello:3, scala:2 (only these meet minFrequency >= 2)
    assert(result.size == 2)
    assert(result.head._1 == "hello")
    assert(result.head._2 == 3)

  test("streaming behavior - each emission is independent"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 3,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("a", "b", "c", "x", "y", "z")
    val output = logic.process(input).toList

    // First emission: [a, b, c]
    // Last emission: [x, y, z]
    // Verify complete change in window content
    val firstWords = output.head.map(_._1).toSet
    val lastWords = output.last.map(_._1).toSet
    
    assert(firstWords.intersect(lastWords).isEmpty)

  test("word removed from counts when count reaches zero"):
    val logic = new TopWordsLogic(
      minLength = 1,
      windowSize = 2,
      howMany = 10,
      everyKSteps = 1,
      minFrequency = 1
    )

    val input = Iterator("a", "b", "c")
    val output = logic.process(input).toList

    // Output 1: [a, b] -> a:1, b:1
    assert(output.head.size == 2)
    // Output 2: [b, c] -> b:1, c:1 (a is gone)
    assert(output(1).size == 2)
    assert(!output(1).exists(_._1 == "a"))