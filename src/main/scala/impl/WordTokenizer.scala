package impl

object WordTokenizer:

  def fromStdin(): Iterator[String] =
    import scala.language.unsafeNulls
    scala.io.Source.stdin
      .getLines()
      .flatMap(_.split("(?U)[^\\p{Alpha}0-9']+"))
      .filter(_.nonEmpty)
