package main

import impl.{TopWordsLogic, WordTokenizer}
import scala.io.Source

object TopWordsMain:
  def main(args: Array[String]): Unit =
    val howMany   = args(0).toInt
    val minLength = args(1).toInt
    val windowSize = args(2).toInt

    val words =
      WordTokenizer.tokenize(Source.stdin.getLines())

    val logic =
      new TopWordsLogic(
        minLength = minLength,
        windowSize = windowSize,
        howMany = howMany
      )

    logic.process(words).foreach { top =>
      println(
        top.map { case (w, n) => s"$w: $n" }.mkString(" ")
      )
    }