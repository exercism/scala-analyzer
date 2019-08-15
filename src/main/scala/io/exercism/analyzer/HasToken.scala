package io.exercism.analyzer

import io.exercism.analyzer.TokenRequirement.{Fail, ReferToMentor, Require, Warn}

sealed trait TokenRequirement
sealed trait Require extends TokenRequirement
sealed trait Warn extends TokenRequirement
sealed trait Fail extends TokenRequirement
sealed trait ReferToMentor extends TokenRequirement

object TokenRequirement {
  case object Require extends Require
  case object Warn extends Warn
  case object Fail extends Fail
  case object ReferToMentor extends ReferToMentor
}

trait HasToken {
  def requirement: TokenRequirement = Require
  def comment: Option[Comment] = None
}

trait HasTokenWarn extends HasToken {
  override def requirement = Warn
}

trait HasTokenFail extends HasToken {
  override def requirement = Fail
}

trait HasTokenReferToMentor extends HasToken {
  override def requirement = ReferToMentor
}
