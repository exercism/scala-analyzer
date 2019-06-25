package io.exercism.analyzer

case class Comment(comment: String, params: Map[String, String] = Map())
case class Analysis(status: String, comments: List[Comment])
