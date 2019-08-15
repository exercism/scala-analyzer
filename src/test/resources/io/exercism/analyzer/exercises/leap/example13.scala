import Leap.isDivisibleBy

object Leap {
  def isDivisibleBy(year: Int, divBy: Int): Boolean = year % divBy == 0

  def leapYear(year: Int): Boolean = (!isDivisibleBy(year, 100) || isDivisibleBy(year, 400)) && isDivisibleBy(year, 4)
}