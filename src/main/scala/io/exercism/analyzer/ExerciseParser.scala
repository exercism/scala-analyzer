package io.exercism.analyzer

import java.io.File
import java.nio.file.InvalidPathException

import scala.meta.Source
import scala.meta.inputs.Input
import scala.meta.parsers.Parsed

object ExerciseParser {
  def parse(filePath: String): Either[Analysis, Source] = {
    try {
      val path = java.nio.file.Paths.get(filePath)
      val bytes = java.nio.file.Files.readAllBytes(path)
      val text = new String(bytes, "UTF-8")
      val input = Input.VirtualFile(path.toString, text)
      input.parse[Source] match {
        case Parsed.Success(src) => Right(src)
        case _ => Left(Analysis("disapprove", List(Comment("scala.general.failed_parse"))))
      }
    } catch {
      case _: InvalidPathException =>
        Left(Analysis("refer_to_mentor", List(Comment("scala.general.file_not_found",
          Map("solutionFile" -> getExerciseFilename(filePath))))))
      case _ : Throwable => Left(Analysis("refer_to_mentor", List(Comment("scala.general.unexpected_exception"))))
    }
  }

  private def getExerciseFilename(filePath: String) = new File(filePath).getName
}
