package io.exercism.analyzer

import scala.meta.Source

trait ExerciseAnalyzer {
  def analyze(source: Source): Analysis
}
