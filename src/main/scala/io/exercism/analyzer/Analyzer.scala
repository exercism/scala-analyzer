package io.exercism.analyzer

import java.io.File

import io.exercism.analyzer.exercises.TwoferAnalyzer

object Analyzer  {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      Console.err.println("usage: analyzer slug exercise-dir")
      return
    }

    val slug = args(0)
    val exerciseDir = args(1)

    val analysis = slug match {
      case "two-fer" => analyzeTwofer(exerciseDir)
      case _ => Analysis("refer_to_mentor", List(Comment("scala.general.exercise_not_found")))
    }

    AnalysisWriter.write(analysis)
  }

  private def analyzeTwofer(exerciseDir: String): Analysis = {
    val filePath = new File(exerciseDir, "Twofer.scala").getAbsolutePath
    ExerciseParser.parse(filePath) match {
      case Left(analysis) => analysis
      case Right(source) => new TwoferAnalyzer().analyze(source)
    }
  }
}
