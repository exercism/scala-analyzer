package io.exercism.analyzer

import org.scalatest.{FunSuite, Matchers}

class ExerciseParserTest extends FunSuite with Matchers {

  test("valid parse") {
    val either = ExerciseParser.parse("./src/test/resources/io/exercism/analyzer/exercises/two-fer/example.scala")
    either should be ('right)
  }

  test("bad filename") {
    val either = ExerciseParser.parse("./src/test/resources/io/exercism/analyzer/exercises/two-fer/Twofer.scala")
    either should be (Left(Analysis(AnalysisStatuses.ReferToMentor, List(Comment("scala.general.file_not_found",
      Map("solutionFile" -> "Twofer.scala"))))))
  }

  test("bad path") {
    val either = ExerciseParser.parse("./src/test/resources/io/exercism/analyzer/exercises/bogus/Twofer.scala")
    either should be (Left(Analysis(AnalysisStatuses.ReferToMentor, List(Comment("scala.general.file_not_found",
      Map("solutionFile" -> "Twofer.scala"))))))
  }
}
