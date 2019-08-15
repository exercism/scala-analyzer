object Leap {
  def leapYear(year: Int): Boolean = {
    year match {
      case x if (x % 400 == 0) => true
      case x if (x % 100 == 0) => false
      case x if (x % 4   == 0) => true
      case _ => false
    }
  }
}