package project2

import breeze.linalg.{DenseVector, DenseMatrix}
import breeze.linalg.functions._

import scala.collection.mutable.ArrayBuffer

/**
  * Implementation based on https://en.wikipedia.org/wiki/DBSCAN
  */
object DBSCAN {

  def apply(dataSet: DenseMatrix[Double], ε: Double, minPts: Int): DenseVector[Double] = {
    var c = 0
    var clusters: Map[Int, Int] = Map.empty
    var visited: Seq[Int] = Nil

    def expandCluster(pt: Int, neighbors: ArrayBuffer[Int]): Unit = {
      clusters += pt -> c
      for (n <- neighbors) {
        if (!(visited contains n)) {
          visited = visited :+ n
          val neighborPts = regionQuery(pt)
          if (neighborPts.size >= minPts) neighbors ++= neighborPts
        }
        if (!clusters.contains(n)) clusters += n -> c
      }
    }

    def regionQuery(pt: Int): Seq[Int] =
      (0 until dataSet.rows).filter { n =>
        euclideanDistance(dataSet.row(pt).toDenseVector, dataSet.row(n).toDenseVector) <= ε
      }

    for (pt <- 0 until dataSet.rows) {
      if (!(visited contains pt)) {
        visited = visited :+ pt
        val neighborPts = regionQuery(pt)
        if (neighborPts.size < minPts) {
          // mark as noise
          clusters += pt -> -1
        } else {
          c += 1
          expandCluster(pt, neighborPts.to[ArrayBuffer])
        }
      }
    }

    DenseVector(clusters.toArray.sortBy(_._1).map(_._2.toDouble))
  }

}
