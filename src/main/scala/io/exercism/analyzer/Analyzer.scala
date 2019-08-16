package io.exercism.analyzer

import java.io.File

import cats.effect.SyncIO
import cats.implicits._
import io.exercism.analyzer.exercises.leap.LeapAnalyzer
import io.exercism.analyzer.exercises.twofer.TwoferAnalyzer

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
      case "leap" => analyzeLeap(exerciseDir)
      case _ => Analysis(AnalysisStatuses.ReferToMentor, List(Comment("scala.general.exercise_not_found")))
    }

    AnalysisWriter.write(analysis, exerciseDir)
  }

  private def analyzeTwofer(exerciseDir: String): Analysis = {
    val optimalSolutions = getOptimalSolutions("two-fer")
    if (optimalSolutions.exists(e => e.isLeft))
      return getFirstAnalysis(optimalSolutions)

    val optimalSolutionSrcs = getSources(optimalSolutions)

    val filePath = getPathToSolution(exerciseDir, "Twofer.scala")
    analyze(filePath, optimalSolutionSrcs,
      (source: Source) => new TwoferAnalyzer().analyze(source, optimalSolutionSrcs))
  }

  private def analyzeLeap(exerciseDir: String): Analysis = {
    val optimalSolutionSrcs = Nil

    val filePath = getPathToSolution(exerciseDir, "Leap.scala")
    analyze(filePath, optimalSolutionSrcs,
      (source: Source) => new LeapAnalyzer().analyze(source, optimalSolutionSrcs))
  }

  private def getPathToSolution(exerciseDir: String, fileName: String): String =
    new File(new File(exerciseDir, "src/main/scala"), fileName).getAbsolutePath

  private def analyze(filePath: String,
                      optimalSolutionSrcs: List[Source],
                      analyzer: Source => Analysis): Analysis = {
    ExerciseParser.parse(filePath) match {
      case Left(analysis) => analysis
      case Right(source) => SyncIO{analyzer.apply(source)}.unsafeRunSync()
    }
  }

  private def getOptimalSolutions(slug: String): List[Either[Analysis, Source]] = {
    val solutions = new File("./optimal-solutions", slug).listFiles {file => "scala" == getFileExtension(file) }
    solutions.map(solution => ExerciseParser.parse(solution.getAbsolutePath)).toList
  }

  private def getFirstAnalysis(eithers: List[Either[Analysis, Source]]): Analysis =
    eithers.find(e => e.isLeft) match {
      case Some(Left(analysis: Analysis)) => analysis
      case _ => Analysis(AnalysisStatuses.ReferToMentor, List(Comment("scala.general.unexpected_exception")))
    }

  private def getSources(eithers: List[Either[Analysis, Source]]): List[Source] =
    eithers.sequence match {
      case Left(_) => List()
      case Right(l) => l
    }

  private def getFileExtension(file: File): String = {
    val reg_ex = """.*\.(\w+)""".r

    file.getName match {
      case reg_ex(ext) => ext
      case _ => ""
    }
  }
}
