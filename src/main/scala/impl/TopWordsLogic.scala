package impl
class TopWordsLogic(
  minLength: Int,
  windowSize: Int,
  howMany: Int,
  everyKSteps: Int,
  minFrequency: Int
):

  private case class State(
    window: Vector[String],
    counts: Map[String, Int],
    steps: Int
  )

  def process(words: Iterator[String]): Iterator[Seq[(String, Int)]] =
    words
      .map(_.toLowerCase)
      .filter(_.length >= minLength)
      .scanLeft(State(Vector.empty, Map.empty, 0)) { (state, word) =>

        val newWindow = state.window :+ word

        val (finalWindow, removed) =
          if newWindow.size > windowSize then
            (newWindow.tail, Some(newWindow.head))
          else
            (newWindow, None)

        val inc =
          state.counts.updatedWith(word):
            case Some(n) => Some(n + 1)
            case None    => Some(1)

        val dec =
          removed match
            case Some(w) =>
              inc.updatedWith(w):
                case Some(1) => None
                case Some(n) => Some(n - 1)
                case None    => None
            case None => inc

        State(finalWindow, dec, state.steps + 1)
      }
      .filter(_.window.size == windowSize)
      .filter(_.steps % everyKSteps == 0)
      .map { state =>
        state.counts.toSeq
          .filter(_._2 >= minFrequency)
          .sortBy { case (w, n) => (-n, w) }
          .take(howMany)
      }