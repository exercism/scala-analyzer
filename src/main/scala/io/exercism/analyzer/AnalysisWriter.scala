package io.exercism.analyzer

import java.io.{File, PrintWriter}
import io.circe.generic.auto._
import io.circe.syntax._

object AnalysisWriter {
  def write(analysis: Analysis) {
    val json = analysis.asJson

    try {
      val writer = new PrintWriter(new File("./analysis.json"))
      writer.write(json.toString())
      writer.close()
    } catch {
      case _: Throwable => Console.err.println("Error writing analysis.json")
    }
  }
}
