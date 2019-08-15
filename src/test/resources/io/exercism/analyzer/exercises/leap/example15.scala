object Leap {
  def leapYear(yr: Int): Boolean = {
    def divisibleBy(i: Int) = yr % i == 0
    divisibleBy(4) && (divisibleBy(400) || !divisibleBy(100))
  }

  def main(args: Array[String]): Unit = {
    leapYear(10)
  }
}