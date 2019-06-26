package io.exercism.analyzer

import scala.meta.Source
import scala.meta.contrib._

trait ExerciseAnalyzer {
  def analyze(source: Source, optimalSolutions: List[Source]): Analysis

  def matchesOptimal(source: Source, optimalSolutions: List[Source]): Boolean =
    optimalSolutions.exists(opt =>
      opt.children.headOption.exists(optTree => source.children.headOption.exists(srcTree => srcTree.isEqual(optTree))))
}
