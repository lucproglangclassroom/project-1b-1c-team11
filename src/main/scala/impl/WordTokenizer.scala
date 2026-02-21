package impl

trait Tokenizer:
  def tokenize(lines: Iterator[String]): Iterator[String]

object WordTokenizer extends Tokenizer:

  private val SplitRegex =
    "(?U)[^\\p{Alpha}0-9']+"

  override def tokenize(lines: Iterator[String]): Iterator[String] =
    lines
      .flatMap(_.split(SplitRegex))
      .filter(_.nonEmpty)