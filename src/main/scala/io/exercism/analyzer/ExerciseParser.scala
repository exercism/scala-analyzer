package io.exercism.analyzer

import java.nio.file.{InvalidPathException, NoSuchFileException}

import cats.effect.SyncIO

import scala.meta.Source
import scala.meta.inputs.Input
import scala.meta.parsers.Parsed
import scala.util.Try

object ExerciseParser {
  def parse(filePath: String): Either[Analysis, Source] = {
    val syncIo =
      SyncIO {
        val either = Try {
          val path = java.nio.file.Paths.get(filePath)
          val bytes = java.nio.file.Files.readAllBytes(path)
          val text = new String(bytes, "UTF-8")
          Input.VirtualFile(path.toString, text)
        }
        .toEither

        either match {
          case Right(input: Input) => input.parse[Source] match {
            case Parsed.Success(src) => Right(src)
            case _ => Left(Analysis(AnalysisStatuses.Disapprove, List(Comment("scala.general.failed_parse"))))
          }
          case Left(t) => t match {
            case _@(_: InvalidPathException | _: NoSuchFileException) =>
              Left(Analysis(AnalysisStatuses.ReferToMentor, List(Comment("scala.general.file_not_found",
                Map("solutionFile" -> filePath)))))
            case _: Throwable => Left(Analysis(AnalysisStatuses.ReferToMentor, List(Comment("scala.general.unexpected_exception"))))
          }
        }
      }

    syncIo.unsafeRunSync()
  }
}
