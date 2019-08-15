object Leap {
  def isDivisibleBy(year: Int, divBy: Int): Boolean = year % divBy == 0

  def leapYear(year: Int): Boolean = isDivisibleBy(year, 4) && (!isDivisibleBy(year, 100) || isDivisibleBy(year, 400))
}