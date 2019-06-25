object Twofer {
  def twofer(name: String = "you"): String =
    if (name.isEmpty) "One for you, one for me." else s"One for $name, one for me."
}
