package impl

class TopWordsLogic(
  minLength: Int,
  windowSize: Int,
  howMany: Int,
  observer: OutputObserver
):

  private val window = new MovingWindow(windowSize)
  private var seen = 0

  def process(word: String): Unit =
    if word.length >= minLength then
      seen += 1
      window.add(word)

      if seen >= windowSize then
        val top =
          window.frequencies
            .toSeq
            .sortBy { case (w, f) => (-f, w) }
            .take(howMany)

        observer.onUpdate(top)
