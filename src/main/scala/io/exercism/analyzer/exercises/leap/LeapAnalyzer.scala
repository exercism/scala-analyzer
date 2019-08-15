package io.exercism.analyzer.exercises.leap

import io.exercism.analyzer._

import TokenExtractors._
import scala.meta._

class LeapAnalyzer extends ExerciseAnalyzer {
  object LeapHasToken {
    val RequiredHasTokens = List(HasClassLeap, HasFunctionLeapYear,
      HasReturnType)

    case object HasClassLeap extends HasToken {
      override def comment = Some(Comment("scala.general.proper_object_name",
        Map("objectName" -> "Leap")))
    }
    case object HasFunctionLeapYear extends HasToken  {
      override def comment = Some(Comment("scala.general.proper_function_name",
        Map("functionName" -> "leapYear")))
    }
    case object HasReturnType extends HasToken {
      override def comment = Some(Comment("scala.general.no_return_type"))
    }


    val ReferToMentorHasTokens = List(HasLeapExpression)

    case object HasLeapExpression extends  HasTokenReferToMentor {
      override def comment = Some(Comment("scala.leap.no_leap_expression"))
    }

    case object HasIfStatement extends HasTokenFail {
      override def comment = Some(Comment("scala.leap.no_conditionals"))
    }
    case object HasMatchStatement extends HasTokenFail {
      override def comment = Some(Comment("scala.leap.no_conditionals"))
    }
    case object HasForStatement extends HasTokenFail {
      override def comment = Some(Comment("scala.leap.no_loops"))
    }
    case object HasDateApiCall extends HasTokenFail {
      override def comment = Some(Comment("scala.leap.no_date_api_call"))
    }
    case object HasReturnStatement extends HasTokenFail {
      override def comment = Some(Comment("scala.general.unnecessary_return"))
    }
    case object HasHardCodedTestCase extends HasTokenFail {
      override def comment = Some(Comment("scala.general.hard_coded_test_cases"))
    }
    case object HasUnneededFunction extends HasTokenFail {
      override def comment = Some(Comment("scala.general.unnecessary_function"))
    }
    case object HasMainFunction extends HasTokenFail {
      override def comment = Some(Comment("scala.general.has_main_function"))
    }
    case object HasConsoleWrite extends HasTokenFail {
      override def comment = Some(Comment("scala.general.do_not_write_to_console"))
    }
  }

  import LeapHasToken._

  override def analyze(source: Source, optimalSolutions: List[Source]): Analysis = {
    if (matchesOptimal(source, optimalSolutions))
      Analysis(AnalysisStatuses.Approve)
    else {
      val hasTokens = source
        .collect {
          case Defn.Object(_, Term.Name("Leap"), _) => HasClassLeap
          case Defn.Def(_, Term.Name("leapYear"), _, pparams, _, _) =>
            if (!pparams.headOption.exists(_.nonEmpty)) HasUnneededFunction else HasFunctionLeapYear
          case Defn.Def(_, Term.Name("main"), _, _, _, _) => HasMainFunction
          case BooleanReturn(_) => HasReturnType
          case Term.If(_) => HasIfStatement
          case Term.Match(_) => HasMatchStatement
          case Term.For(_) => HasForStatement
          case Term.ForYield(_) => HasForStatement
          case Term.Return(_) => HasReturnStatement
          case SimpleLeapExpression(_) => HasLeapExpression
          case CallingFunction(_) => HasLeapExpression
          case Term.Name("LocalDate") => HasDateApiCall
          case Term.Name("println") => HasConsoleWrite
          case Term.Name("print") => HasConsoleWrite
        }

      val acc = mapRequiredHasTokens(hasTokens)
      val acc2 = mapReferToMentorHasTokens(hasTokens, acc)
      mapDisapproveHasTokens(hasTokens, acc2)
    }
  }

  private def mapRequiredHasTokens(hasTokens: List[HasToken]): Analysis = {
    RequiredHasTokens
      .find(!hasTokens.contains(_))
      .map(hasToken => Analysis(AnalysisStatuses.Disapprove, addComment(hasToken.comment, List())))
      .getOrElse(Analysis(AnalysisStatuses.Approve, List()))
  }

  private def mapReferToMentorHasTokens(hasTokens: List[HasToken],
                                        analysis: Analysis): Analysis = {
    ReferToMentorHasTokens
        .find(!hasTokens.contains(_))
        .map(hasToken => Analysis(AnalysisStatuses.ReferToMentor, addComment(hasToken.comment, List())))
        .foldLeft(analysis)((analysis, acc) =>
          if (acc.status == AnalysisStatuses.Disapprove)
            acc
          else
            Analysis(AnalysisStatuses.ReferToMentor, analysis.comments ++ acc.comments))
  }

  private def mapDisapproveHasTokens(hasTokens: List[HasToken],
                                     analysis: Analysis): Analysis = {
    hasTokens
      .filter(hasToken => hasToken.requirement != TokenRequirement.Require && hasToken.requirement != TokenRequirement.ReferToMentor)
      .foldRight(analysis)((hasToken, acc) =>
        if (acc.status == AnalysisStatuses.ReferToMentor)
          Analysis(AnalysisStatuses.Disapprove, addComment(hasToken.comment, Nil))
        else
          Analysis(AnalysisStatuses.Disapprove, addComment(hasToken.comment, acc.comments)))
  }

  private def addComment(comment: Option[Comment],
                         comments: List[Comment]): List[Comment] = {
    comment match {
      case None => comments
      case Some(c) => (c +: comments).distinct
    }
  }
}
