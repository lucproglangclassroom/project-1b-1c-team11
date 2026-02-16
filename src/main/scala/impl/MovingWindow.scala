package impl

import scala.collection.mutable

class MovingWindow(size: Int):
  private val queue = mutable.Queue[String]()
  private val counts = mutable.Map[String, Int]().withDefaultValue(0)

  def add(word: String): Unit =
    queue.enqueue(word)
    counts(word) += 1

    if queue.size > size then
      val removed = queue.dequeue()
      counts(removed) -= 1
      if counts(removed) == 0 then counts -= removed

  def frequencies: Map[String, Int] =
    counts.toMap
