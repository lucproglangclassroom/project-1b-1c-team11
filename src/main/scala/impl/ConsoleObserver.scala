package impl

class ConsoleObserver extends OutputObserver:
  override def onUpdate(stats: Seq[(String, Int)]): Unit =
    println(stats.map { case (w, f) => s"$w: $f" }.mkString(" "))
