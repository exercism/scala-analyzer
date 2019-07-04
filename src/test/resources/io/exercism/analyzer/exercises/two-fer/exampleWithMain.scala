object Twofer {
  def twofer(name: String = "you"): String =
    s"One for $name, one for me."

  def main(args: Array[String]): Unit = {
    twofer("beto")
  }
}
