package impl

trait OutputObserver:
  def onUpdate(stats: Seq[(String, Int)]): Unit
