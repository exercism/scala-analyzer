import java.time.{DateTimeException, LocalDate, Month}

object Leap {
  def leapYear(year: Int): Boolean =
    try {
      LocalDate.of(year, Month.FEBRUARY, 29)
      true
    } catch {
      case ex: DateTimeException => false
    }
}