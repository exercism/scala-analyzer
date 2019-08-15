object Leap {

  def divBy(divisor: Int): Int => Boolean = {
    dividend => dividend % divisor == 0
  }

  def leapYear(year: Int): Boolean = {
    val divBy4 = divBy(4)
    val divBy100 = divBy(100)
    val divBy400 = divBy(400)

    if(divBy4(year)) {
      val isDivisbleBy100 = divBy100(year)
      if(isDivisbleBy100 && divBy400(year)) {
        true
      }
      else if(isDivisbleBy100) {
        false
      }
      else {
        true
      }
    } else {
      false
    }
  }
}