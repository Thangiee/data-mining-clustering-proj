package project2

import project2.Evaluator.{RealLabel, clusterLabel}

import scala.collection._

case class Evaluator(testResult: Seq[(RealLabel, clusterLabel)]) {
  private val clusters = testResult.map(_._2).distinct.toVector

  def purity: Double = {
    clusters.map(c => {
      val clusterLabels = testResult.filter(_._2 == c).map(_._1)
      val majLabel = majority(clusterLabels)
      clusterLabels.count(_ == majLabel)
    }).sum.toDouble / testResult.size
  }

  private def majority(labels: Seq[RealLabel]): RealLabel = {
    labels.groupBy(l => l).maxBy(_._2.size)._1
  }
}

object Evaluator {
  type RealLabel = String
  type clusterLabel = String
}
