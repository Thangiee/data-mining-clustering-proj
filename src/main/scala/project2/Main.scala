package project2

import java.awt.Color
import java.io.File

import breeze.linalg._
import breeze.numerics._
import breeze.plot._

import scala.io.Source

object Main extends App {

  def reduceDimensions(data: DenseMatrix[Double], targetDimen: Int): DenseMatrix[Double] = {
    val svd.SVD(u, s, v) = svd(data)

    val k = 0 until targetDimen
    val n = 0 until data.rows
    val d = 0 until targetDimen

    //N x K     K x K      K x D transposed
    u(n, k) * diag(s(k)) * v(k, d).t
  }

  def makeScatterPlot(data: DenseMatrix[Double], labels: DenseVector[Double]): Series = {
    val x = data(::, 0)
    val y = data(::, 1)

    val xRange = max(data(::, 0 to 0)) - min(data(::, 0 to 0))
    val yRange = max(data(::, 1 to 1)) - min(data(::, 1 to 1))
    val range = if (xRange > yRange) xRange else yRange

    // map class to color
    val classColor = Map(
      -1.0 -> Color.GRAY, // noise class for DBSCAN
      1.0 -> Color.RED,
      2.0 -> Color.BLUE,
      3.0 -> Color.GREEN,
      4.0 -> Color.YELLOW,
      5.0 -> Color.ORANGE,
      6.0 -> Color.CYAN,
      7.0 -> Color.MAGENTA,
      8.0 -> Color.PINK
    )

    scatter(
      x, y,
      size = i =>  0.01 + .02*log(range) ,
      colors = i => classColor.getOrElse(labels(i), Color.BLACK),
      tips = i => s"class ${labels(i)}"
    )
  }

  val usage =
    """
      |Usage:
      | java -jar project2.jar <data_set_file.csv> svd <dimen>
      | java -jar project2.jar <data_set_file.csv> plot <label_file.csv>
      | java -jar project2.jar <data_set_file.csv> kmeans <k>
      | java -jar project2.jar <data_set_file.csv> dbscan <label_file.csv> <eps> <min_pts>
      |
      | The arguments provide to the program the following information:
      |   *  The first argument is the name of the data set file, it should
      |      be a csv file and it should not contains label information.
      |   *  The second argument can have four possible values: SVD, plot, kmeans, or dbscan.
      |
      |   *  <dimen> argument is an integer that specify the target dimensions to reduce to.
      |   *  <label_file.csv> argument is the name of the file with labels information.
      |   *  <k> argument is the number of clusters for KMeans algorithm.
      |   *  <eps> argument is the distance threshold.
      |   *  <min_pts> is the minimum number of points within eps.
    """.stripMargin

  val fName   = args(0).split('.').head
  val dataSet = csvread(new File(args(0)))

  args(1).toLowerCase match {
    case "svd" =>
      val dimen = args(2).toInt
      val newDataSet = reduceDimensions(dataSet, dimen)
      val out = new File(s"$fName-svd.csv")
      csvwrite(out, newDataSet)
      println(s"Result saved to: ${out.getAbsolutePath}")

    case "plot" =>
      val labelData = csvread(new File(args(2))).toDenseVector
      val figure = Figure(fName)
      figure.subplot(0) += makeScatterPlot(dataSet, labelData)
      figure.saveas(s"$fName-plot.png", dpi=180)

    case "kmeans" =>
      val k = args(2).toInt
      val result = KMeans(dataSet, k)
      val figure = Figure(s"$fName k=$k")
      // draw centroids
      val x = result.centroids.col(0).toDenseVector
      val y = result.centroids.col(1).toDenseVector
      figure.subplot(0) += plot(x, y, '+')
      figure.subplot(0) += scatter(x, y, i => .1)
      // draw kmean result
      figure.subplot(0) += makeScatterPlot(dataSet, result.labels)
      figure.saveas(s"$fName-kmeans-plot.png", dpi=180)
      println(s"SSE=${KMeans.evaluate(dataSet, result)}")

    case "dbscan" =>
      val eps = args(3).toDouble
      val minPts = args(4).toInt
      val labelData = Source.fromFile(args(2)).getLines().toSeq
      val result = DBSCAN(dataSet, eps, minPts)
      val figure = Figure(s"$fName eps=$eps min_pts=$minPts")
      figure.subplot(0) += makeScatterPlot(dataSet, result)
      figure.saveas(s"$fName-DBSCAN-plot.png", dpi=180)
      val purity = Evaluator(labelData zip result.map(_.toInt.toString).toScalaVector()).purity
      println(s"Purity=$purity")

    case _ => println(usage)
  }
}
