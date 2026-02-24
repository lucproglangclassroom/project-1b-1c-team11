package main

import impl.{TopWordsLogic, WordTokenizer}
import scala.io.Source
import sun.misc.Signal

object TopWordsMain:

  def main(args: Array[String]): Unit =

    // --- SIGPIPE handling (Project 1c requirement) ---
    // Prevents JVM crash when output is piped into commands like `head`
    Signal.handle(new Signal("PIPE"), _ => ())

    // --- Default arguments (match assignment spec) ---
    val howMany      = if args.length > 0 then args(0).toInt else 10
    val minLength    = if args.length > 1 then args(1).toInt else 6
    val windowSize   = if args.length > 2 then args(2).toInt else 1000
    val everyKSteps  = if args.length > 3 then args(3).toInt else 10
    val minFrequency = if args.length > 4 then args(4).toInt else 3

    // --- Read words from stdin ---
    val words =
      WordTokenizer.tokenize(Source.stdin.getLines())

    // --- Pure functional logic ---
    val logic =
      new TopWordsLogic(
        minLength = minLength,
        windowSize = windowSize,
        howMany = howMany,
        everyKSteps = everyKSteps,
        minFrequency = minFrequency
      )

    // --- Print results ---
    logic.process(words).foreach { topWords =>
      println(
        topWords
          .map { case (w, n) => s"$w: $n" }
          .mkString(" ")
      )
    }