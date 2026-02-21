package impl

import org.scalatest.funsuite.AnyFunSuite

class TopWordsLogicSpec extends AnyFunSuite:

  test("emits output once window is full"):
    val logic =
      new TopWordsLogic(
        minLength = 1,
        windowSize = 3,
        howMany = 2
      )

    val input = Iterator("a", "b", "a", "c")

    val output =
      logic.process(input).toList

    assert(output.nonEmpty)
    assert(output.head.exists(_._1 == "a"))