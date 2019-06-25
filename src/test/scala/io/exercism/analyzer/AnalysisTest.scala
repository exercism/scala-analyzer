package io.exercism.analyzer

import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.{FunSuite, Matchers}

class AnalysisTest extends FunSuite with Matchers {
  test("test serialize") {
    val analysis = Analysis("status", List(Comment("comment1", Map("foo" -> "bar"))))
    val json = analysis.asJson
    val analysisFromJson = json.as[Analysis]
    analysisFromJson should be (Right(analysis))
  }
}
