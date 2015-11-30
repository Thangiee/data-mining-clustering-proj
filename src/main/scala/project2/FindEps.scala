package project2

import java.io.File

import breeze.linalg._
import breeze.linalg.functions._
import breeze.plot._

object FindEps extends App {

  val dataSet = csvread(new File("iris-svd.csv")).toVectors

  val distsTo15thNN = dataSet.map { pt =>
    val nn = dataSet.map(neighbor => euclideanDistance(pt, neighbor)).sorted
    nn(15)
  }

  val figure = Figure()
  val p = figure.subplot(0)

  p += plot((1 to distsTo15thNN.size).map(_.toDouble), DenseVector(distsTo15thNN.sorted.toArray))
  p.xlabel = "Points sorted according to distance to 15th nearest neighbor"
  p.ylabel = "15th Nearest Neighbor Distance"

  figure.saveas("distNN.png")
}
