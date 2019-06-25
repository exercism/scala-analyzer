package io.exercism.analyzer.exercises

import io.exercism.analyzer.{Analysis, Comment}
import org.scalatest.{FunSuite, Matchers}

import scala.meta.Source
import scala.meta.inputs.Input

class TwoferAnalyzerTest extends FunSuite with Matchers {
  private val twoferAnalyzer = new TwoferAnalyzer()

  test("optimal solution") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/example.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("approve_as_optimal", List()))
  }

  test("with alt interpolate statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithAltInterpolate.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("approve_as_optimal", List()))
  }

  test("with if statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithIf.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("disapprove_with_comment", List(Comment("scala.two-fer.no_conditionals"))))
  }

  test("with match statement") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithMatch.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("disapprove_with_comment", List(Comment("scala.two-fer.no_conditionals"))))
  }

  test("without interpolate") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithoutInterpolate.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("disapprove_with_comment", List(Comment("scala.two-fer.use_string_interpolate"))))
  }

  test("with hardcoded test case") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithHardcodedTestCase.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("disapprove_with_comment", List(Comment("scala.general.hard_coded_test_cases"))))
  }

  test("with multiple errors") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithMultErrors.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("disapprove_with_comment", List(Comment("scala.two-fer.no_conditionals"),
      Comment("scala.two-fer.no_default_param"))))
  }

  test("without return type") {
    val source = getSource("./src/test/resources/io/exercism/analyzer/exercises/two-fer/exampleWithoutReturnType.scala")
    val analysis = twoferAnalyzer.analyze(source)
    analysis should be (Analysis("disapprove_with_comment", List(Comment("scala.two-fer.no_return_type"))))
  }

  private def getSource(srcpath: String): Source = {
    val path = java.nio.file.Paths.get(srcpath)
    val bytes = java.nio.file.Files.readAllBytes(path)
    val text = new String(bytes, "UTF-8")
    val input = Input.VirtualFile(path.toString, text)
    input.parse[Source].get
  }
}
