package io.exercism.analyzer.exercises.twofer

import io.exercism.analyzer._

import scala.meta._

class TwoferAnalyzer extends ExerciseAnalyzer {
  object TwoferHasToken {
    val RequiredHasTokens = List(HasClassTwofer, HasFunctionTwofer, HasDefaultParam,
      HasReturnType, HasInterpolate)

    case object HasClassTwofer extends HasToken {
      override def comment = Some(Comment("scala.general.proper_object_name",
        Map("objectName" -> "Twofer")))
    }
    case object HasFunctionTwofer extends HasToken  {
      override def comment = Some(Comment("scala.general.proper_function_name",
        Map("functionName" -> "twofer")))
    }
    case object HasDefaultParam extends HasToken {
      override def comment = Some(Comment("scala.two-fer.no_default_param"))
    }
    case object HasReturnType extends HasToken {
      override def comment = Some(Comment("scala.general.no_return_type"))
    }
    case object HasInterpolate extends HasToken {
      override def comment = Some(Comment("scala.general.use_string_interpolate"))
    }

    case object HasIfStatement extends HasTokenFail {
      override def comment = Some(Comment("scala.two-fer.no_conditionals"))
    }
    case object HasMatchStatement extends HasTokenFail {
      override def comment = Some(Comment("scala.two-fer.no_conditionals"))
    }
    case object HasForStatement extends HasTokenFail {
      override def comment = Some(Comment("scala.two-fer.no_loops"))
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
    case object HasEmptyDefaultParam extends HasTokenFail {
      override def comment = Some(Comment("scala.two-fer.empty_default_param"))
    }
    case object HasMainFunction extends HasTokenFail {
      override def comment = Some(Comment("scala.general.has_main_function"))
    }
    case object HasConsoleWrite extends HasTokenFail {
      override def comment = Some(Comment("scala.general.do_not_write_to_console"))
    }
  }

  import TwoferHasToken._

  override def analyze(source: Source, optimalSolutions: List[Source]): Analysis = {
    if (matchesOptimal(source, optimalSolutions))
      Analysis(AnalysisStatuses.Approve)
    else {
      val hasTokens = source
        .collect {
          case Defn.Object(_, Term.Name("Twofer"), _) => HasClassTwofer
          case Defn.Def(_, Term.Name("twofer"), _, pparams, _, _) =>
            if (!pparams.headOption.exists(_.nonEmpty)) HasUnneededFunction else HasFunctionTwofer
          case Defn.Def(_, Term.Name("main"), _, _, _, _) => HasMainFunction
          case Term.Param(_, _, _, Some(Lit.String(""))) => HasEmptyDefaultParam
          case Term.Param(_, _, _, Some(Lit.String("you"))) => HasDefaultParam
          case t if isReturnType(t) => HasReturnType
          case Term.Interpolate(_) => HasInterpolate
          case Term.If(_) => HasIfStatement
          case Term.Match(_) => HasMatchStatement
          case Term.For(_) => HasForStatement
          case Term.ForYield(_) => HasForStatement
          case Term.Return(_) => HasReturnStatement
          case Term.Name("println") => HasConsoleWrite
          case Term.Name("print") => HasConsoleWrite
          case Lit.String("Alice") => HasHardCodedTestCase
          case Lit.String("Bob") => HasHardCodedTestCase
        }

      val acc = mapRequiredHasTokens(hasTokens)
      mapOtherHasTokens(hasTokens, acc)
    }
  }

  private def isReturnType(tree: Tree): Boolean = {
    tree match {
      case Type.Name("String") => tree.parent match {
        case Some(Defn.Def(_, Term.Name("twofer"), _, _, _, _)) => true
        case _ => false
      }
      case _ => false
    }
  }

  private def mapRequiredHasTokens(hasTokens: List[HasToken]): Analysis = {
    RequiredHasTokens
      .find(!hasTokens.contains(_))
      .map(hasToken => Analysis(AnalysisStatuses.Disapprove, addComment(hasToken.comment, List())))
      .getOrElse(Analysis(AnalysisStatuses.ReferToMentor, List()))
  }

  private def mapOtherHasTokens(hasTokens: List[HasToken],
                                analysis: Analysis): Analysis = {
    hasTokens
      .filter(hasToken => hasToken.requirement != TokenRequirement.Require)
      .foldRight(analysis)((hasToken, acc) => Analysis(AnalysisStatuses.Disapprove, addComment(hasToken.comment, acc.comments)))
  }

  private def addComment(comment: Option[Comment],
                         comments: List[Comment]): List[Comment] = {
    comment match {
      case None => comments
      case Some(c) => (c +: comments).distinct
    }
  }
}
