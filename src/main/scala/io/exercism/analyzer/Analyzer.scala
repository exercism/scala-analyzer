package io.exercism.analyzer

import java.io.{File, FileFilter}

import io.exercism.analyzer.exercises.TwoferAnalyzer

import scala.meta.Source

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

    AnalysisWriter.write(analysis, exerciseDir)
  }

  private def analyzeTwofer(exerciseDir: String): Analysis = {
    val optimalSolutions = getOptimalSolutions("two-fer")
    if (optimalSolutions.exists(e => e.isLeft))
      return lefts(optimalSolutions).head

    val filePath = new File(exerciseDir, "Twofer.scala").getAbsolutePath
    ExerciseParser.parse(filePath) match {
      case Left(analysis) => analysis
      case Right(source) => new TwoferAnalyzer().analyze(source, rights(optimalSolutions))
    }
  }

  private def getOptimalSolutions(slug: String): List[Either[Analysis, Source]] = {
    val solutions = new File("./optimal-solutions", slug).listFiles {file => "scala" == getFileExtension(file) }
    solutions.map(solution => ExerciseParser.parse(solution.getAbsolutePath)).toList
  }

  private def lefts(eithers: List[Either[Analysis, Source]]): List[Analysis] =
    eithers.filter(e => e.isLeft).map { case Left(a) => a }

  private def rights(eithers: List[Either[Analysis, Source]]): List[Source] =
    eithers.filter(e => e.isRight).map { case Right(a) => a }

  private def getFileExtension(file: File): String = {
    val reg_ex = """.*\.(\w+)""".r

    file.getName match {
      case reg_ex(ext) => ext
      case _ => ""
    }
  }
}
