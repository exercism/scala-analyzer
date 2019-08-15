package io.exercism.analyzer.exercises.leap

import java.io.File

import io.exercism.analyzer.{Analysis, Comment}
import org.scalatest.{FunSuite, Matchers}

import scala.meta._
import scala.meta.inputs.Input

class LeapAnalyzerTest extends FunSuite with Matchers {
  private val leapAnalyzer = new LeapAnalyzer()

  test("optimal solution - with helper function") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - with helper function - alt ordering") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example9.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - with helper function - at object scope") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example10.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - with helper function - at object scope - alt ordering") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example11.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - with helper function - at object scope - alt ordering - 2") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example12.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - with helper function - at object scope - alt ordering - 3") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example13.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example2.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - alternate order of operations") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example4.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - alternate order of operations - 2") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example16.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - no parens") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example6.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution - alternate not 100") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example3.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("should not contain match statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example5.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.leap.no_conditionals"))))
  }

  test("should not contain if statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example7.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.leap.no_conditionals"))))
  }

  test("should not contain explicit return") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example8.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.unnecessary_return"))))
  }

  test("should not contain println") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example14.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.do_not_write_to_console"))))
  }

  test("should not contain main") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example15.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.has_main_function"))))
  }

  test("should not contain call to LocalDate") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/leap/example17.scala")
    val analysis = leapAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.leap.no_date_api_call"))))
  }

  private val optimalSolutions: List[Source] = {
    val solutions = new File("./optimal-solutions/leap").listFiles
    solutions.map(file => file.getAbsolutePath).map { getSource } toList
  }

  private def getSource(srcpath: String): Source = {
    val path = java.nio.file.Paths.get(srcpath)
    val bytes = java.nio.file.Files.readAllBytes(path)
    val text = new String(bytes, "UTF-8")
    val input = Input.VirtualFile(path.toString, text)
    input.parse[Source].get
  }
}
