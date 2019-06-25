package io.exercism.analyzer.exercises

import io.exercism.analyzer.{Analysis, Comment, ExerciseAnalyzer, HasToken, HasTokenFail, TokenRequirement}

import scala.meta._

class TwoferAnalyzer extends ExerciseAnalyzer {

  object TwoferHasToken {
    val RequiredHasTokens = List(HasClassTwofer, HasFunctionTwofer, HasDefaultParam, HasInterpolate)

    case object HasClassTwofer extends HasToken {
      override def comment = Some(Comment("scala.general.proper_class_and_method_names"))
    }
    case object HasFunctionTwofer extends HasToken  {
      override def comment = Some(Comment("scala.general.proper_class_and_method_names"))
    }
    case object HasDefaultParam extends HasToken {
      override def comment = Some(Comment("scala.two-fer.no_default_param"))
    }
    case object HasInterpolate extends HasToken {
      override def comment = Some(Comment("scala.two-fer.use_string_interpolate"))
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
    case object HasHardCodedTestCase extends HasTokenFail {
      override def comment = Some(Comment("scala.general.hard_coded_test_cases"))
    }
  }

  import TwoferHasToken._

  override def analyze(source: Source): Analysis = {
    val hasTokens = source
      .collect {
        case Defn.Object(_, Term.Name("Twofer"), _) => HasClassTwofer
        case Defn.Def(_, Term.Name("twofer"), _, _, _, _) => HasFunctionTwofer
        case Term.Param(_, _, _, Some(Lit.String("you"))) => HasDefaultParam
        case Term.Interpolate(_) => HasInterpolate
        case Term.If(_) => HasIfStatement
        case Term.Match(_) => HasMatchStatement
        case Term.For(_) => HasForStatement
        case Term.ForYield(_) => HasForStatement
        case Lit.String("Alice") => HasHardCodedTestCase
        case Lit.String("Bob") => HasHardCodedTestCase
      }

    val acc = mapRequiredHasTokens(hasTokens)
    mapOtherHasTokens(hasTokens, acc)
  }

  private def mapRequiredHasTokens(hasTokens: List[HasToken]): Analysis = {
    RequiredHasTokens
      .find(!hasTokens.contains(_))
      .map(hasToken => Analysis("disapprove_with_comment", addComment(hasToken.comment, List())))
      .getOrElse(Analysis("approve_as_optimal", List()))
  }

  private def mapOtherHasTokens(hasTokens: List[HasToken],
                                analysis: Analysis): Analysis = {
    hasTokens
      .filter(hasToken => hasToken.requirement != TokenRequirement.Require)
      .foldRight(analysis)((hasToken, acc) => Analysis("disapprove_with_comment", addComment(hasToken.comment, acc.comments)))
  }

  private def addComment(comment: Option[Comment],
                         comments: List[Comment]): List[Comment] = {
    comment match {
      case None => comments
      case Some(c) => c +: comments
    }
  }
}
