package project2

import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.linalg.functions._

import scala.util.Random._

object KMeans {
  type Point = DenseVector[Double]

  case class KMeansResult(centroids: DenseMatrix[Double], labels: DenseVector[Double])

  def apply(dataSet: DenseMatrix[Double], k: Int): KMeansResult = {
    val dataPoints = dataSet.toVectors
    var centroids = shuffle(dataPoints).take(k)
    var labels    = Seq.fill(dataPoints.size)(0.0)
    var oldCentroids = Seq.empty[Point]

    while (oldCentroids != centroids) {
      oldCentroids = centroids

      labels = getLabels(dataPoints, centroids)
      centroids = getCentroids(dataPoints, labels, k)
    }

    KMeansResult(
      DenseMatrix(centroids.map(_.toScalaVector()):_*),
      DenseVector(labels.map(_ + 1).toArray)
    )
  }

  def evaluate(dataSet: DenseMatrix[Double], res: KMeansResult): Double = {
    (0 until dataSet.rows).map { i =>
      val point = dataSet.row(i).toDenseVector
      val closestCentroid = res.centroids.toVectors(res.labels(i).toInt - 1)
      val dist = euclideanDistance(point, closestCentroid)
      dist * dist
    }.sum
  }

  private def getLabels(dataPoints: Seq[Point], centroids: Seq[Point]): Seq[Double] = {
    val centroidsWithLabel = centroids.zipWithIndex
    // find the closest centroid for each data point
    dataPoints.map(point =>
      centroidsWithLabel.minBy { case (centroid, label) =>
        euclideanDistance(centroid, point)
      }
    ).map(_._2.toDouble) // get the labels
  }

  private def getCentroids(dataPoints: Seq[Point], labels: Seq[Double], k: Int): Seq[Point] = {
    (0 until k).map { label =>
      val cluster = dataPoints.zip(labels).filter(_._2 == label).map(_._1)  // get the points with the same label
      if (cluster.isEmpty) shuffle(dataPoints).head                         // choose new random centroid when cluster is empty
      else DenseVector(mean(cluster.map(_(0))), mean(cluster.map(_(1)))) // avg up the the points to get the new centroid
    }
  }

  private def mean(xs: Seq[Double]): Double = xs.sum / xs.size
}
