object Leap {
  def leapYear(year: Int): Boolean = {
    def divisibleBy(divisor: Int) = year % divisor == 0
    divisibleBy(4) && (!divisibleBy(100) || divisibleBy(400))
  }
}