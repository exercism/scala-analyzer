package io.exercism.analyzer

object AnalysisStatuses {
  type Status = String
  val ReferToMentor: Status = "refer_to_mentor"
  val Disapprove: Status = "disapprove"
  val Approve: Status = "approve"
}

case class Comment(comment: String, params: Map[String, String] = Map())
case class Analysis(status: AnalysisStatuses.Status, comments: List[Comment] = List())
