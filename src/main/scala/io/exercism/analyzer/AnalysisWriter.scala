package io.exercism.analyzer

import java.io.{File, PrintWriter}

import cats.effect.SyncIO
import io.circe.generic.auto._
import io.circe.syntax._

object AnalysisWriter {
  def write(analysis: Analysis,
            exerciseDir: String): Unit = {
    val json = analysis.asJson

    SyncIO(new PrintWriter(new File(exerciseDir, "analysis.json")))
        .bracket{pw => SyncIO(pw.write(json.toString()))} {pw => SyncIO(pw.close())}
    .unsafeRunSync()
  }
}
