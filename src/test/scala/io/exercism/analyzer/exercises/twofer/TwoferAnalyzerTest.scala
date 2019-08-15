package io.exercism.analyzer.exercises.twofer

import java.io.File

import io.exercism.analyzer.{Analysis, Comment}
import org.scalatest.{FunSuite, Matchers}

import scala.meta._
import scala.meta.inputs.Input

class TwoferAnalyzerTest extends FunSuite with Matchers {
  private val twoferAnalyzer = new TwoferAnalyzer()

  test("optimal solution") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/example.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution with extra spaces") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithWhiteSpace.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("optimal solution with function braces") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithFunctionBraces.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("with alt interpolate statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithAltInterpolate.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("approve", List()))
  }

  test("with if statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithIf.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.two-fer.no_conditionals"))))
  }

  test("with match statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithMatch.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.two-fer.no_conditionals"))))
  }

  test("without interpolate") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithoutInterpolate.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.use_string_interpolate"))))
  }

  test("with hardcoded test case") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithHardcodedTestCase.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.hard_coded_test_cases"))))
  }

  test("with multiple errors") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithMultErrors.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.two-fer.no_conditionals"),
      Comment("scala.two-fer.no_default_param"))))
  }

  test("without return type") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithoutReturnType.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.no_return_type"))))
  }

  test("with return") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithReturn.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.unnecessary_return"))))
  }

  test("with multiple functions") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithMultFunctions.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.unnecessary_function"),
      Comment("scala.two-fer.no_default_param"))))
  }

  test("with empty default param") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithEmptyDefaultParam.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.two-fer.empty_default_param"),
      Comment("scala.two-fer.no_conditionals"),
      Comment("scala.two-fer.no_default_param"))))
  }

  test("with main function") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithMain.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.has_main_function"))))
  }

  test("with println") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithPrintln.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.do_not_write_to_console"))))
  }

  test("with print") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithPrint.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.do_not_write_to_console"))))
  }

  test("with System.out.println") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithSystemOutPrintln.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.do_not_write_to_console"))))
  }

  test("with invalid object name") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithIncorrectObjectName.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.proper_object_name", Map("objectName" -> "Twofer")))))
  }

  test("with invalid function name") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithIncorrectFunctionName.scala")
    val analysis = twoferAnalyzer.analyze(source, optimalSolutions)
    analysis should be (Analysis("disapprove", List(Comment("scala.general.proper_function_name", Map("functionName" -> "twofer")))))
  }

  private val optimalSolutions: List[Source] = {
    val solutions = new File("./optimal-solutions/two-fer").listFiles
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
