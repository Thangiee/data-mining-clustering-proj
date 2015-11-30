import breeze.linalg.{DenseVector, DenseMatrix}

package object project2 {

  implicit class DenseMatrixOps[A](matrix: DenseMatrix[A]) {
    def row(i: Int) = matrix(i to i, ::)
    def col(i: Int) = matrix(::, i to i)

    def toVectors: Seq[DenseVector[A]] = (0 until matrix.rows).map(r => matrix.row(r).toDenseVector)
  }
}
