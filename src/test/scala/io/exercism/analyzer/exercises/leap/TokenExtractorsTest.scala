package io.exercism.analyzer.exercises.leap

import org.scalatest.{FunSuite, Matchers}
import scala.meta._
import TokenExtractors._

class TokenExtractorsTest extends FunSuite with Matchers {
  test("optimal solution - with helper function") {
    val statement = "divisibleBy(4) && (divisibleBy(400) || !divisibleBy(100))".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 2") {
    val statement = "divisibleBy(4) && (!divisibleBy(100) || divisibleBy(400))".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 3") {
    val statement = "divisibleBy(400) || (divisibleBy(4) && !divisibleBy(100))".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 4") {
    val statement = "isDivisibleBy(4) && ! isDivisibleBy(100) || isDivisibleBy(400)".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 2 params") {
    val statement = "divBy(year, 4) && (divBy(year, 400) || !div(year, 100))".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 2 params - 2") {
    val statement = "divBy(year, 4) && (!divBy(year, 100) || div(year, 400))".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 2 params - 3") {
    val statement = "(divBy(year, 400) || !divBy(year, 100)) && divBy(year, 4)".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 2 params - 4") {
    val statement = "(!divBy(year, 100) || divBy(year, 400)) && divBy(year, 4)".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution - with helper function - 2 params - 5") {
    val statement = "(isDivisible(year, 4) && !isDivisible(year, 100)) || isDivisible(year, 400)".parse[Stat].get
    statement should matchPattern { case CallingFunction(_) => }
  }

  test("bad solution - with helper function") {
    val statement = "!divisibleBy(4) && (divisibleBy(400) || !divisibleBy(100))".parse[Stat].get
    statement should not  matchPattern { case CallingFunction(_) => }
  }

  test("optimal solution") {
    val statement = "year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)".parse[Stat].get
    statement should matchPattern { case SimpleLeapExpression(_) => }
  }

  test("optimal solution - 2 ") {
    val statement = "year % 4 == 0 && year % 100 != 0 || year % 400 == 0".parse[Stat].get
    statement should matchPattern { case SimpleLeapExpression(_) => }
  }

  test("optimal solution - 3 ") {
    val statement = "year % 4 == 0 && (year % 400 == 0 || year % 100 != 0)".parse[Stat].get
    statement should matchPattern { case SimpleLeapExpression(_) => }
  }

  test("optimal solution - 4 ") {
    val statement = "(year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)".parse[Stat].get
    statement should matchPattern { case SimpleLeapExpression(_) => }
  }

  test("optimal solution - 5 ") {
    val statement = "(yr % 4 == 0 && !(yr % 100 == 0)) || yr % 400 == 0".parse[Stat].get
    statement should matchPattern { case SimpleLeapExpression(_) => }
  }

  test("optimal solution - 6 ") {
    val statement = "y % 400 == 0 || (y % 100 != 0 && y % 4 == 0)".parse[Stat].get
    statement should matchPattern { case SimpleLeapExpression(_) => }
  }

  test("bad solution ") {
    val statement = "y % 400 != 0 || (y % 100 != 0 && y % 4 == 0)".parse[Stat].get
    statement should not matchPattern { case SimpleLeapExpression(_) => }
  }
}
