package main

import impl.*
import scala.io.Source
import java.io.IOException

object TopWordsMain:

  def main(args: Array[String]): Unit =
    // defaults (per assignment)
    val howMany = 10
    val minLength = 6
    val windowSize = 1000

    val observer = new ConsoleObserver
    val logic = new TopWordsLogic(minLength, windowSize, howMany, observer)

    try
      WordTokenizer
        .fromStdin()
        .foreach(logic.process)
    catch
      case _: IOException =>
        () // SIGPIPE: exit quietly
