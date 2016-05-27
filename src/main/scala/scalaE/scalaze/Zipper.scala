package scalaE.scalaze

/**
  * Zippers are basically a stream with a pointer.
  */
object Zipper {

  // from http://stackoverflow.com/questions/23984160/zipper-to-iterate-over-list-in-scala


  /*
  Suppose that we've got a sequence of numbers and we want to do a simple
  form of smoothing with an exponential moving average,
  where the new value for each position in the list is
  an average of the current value and all the other values,
  but with more distant neighbors contributing less.
   */
  import scalaz._, Scalaz._

  val weights = Stream.from(1).map(1.0 / math.pow(2, _))

  def sumNeighborWeights(neighbors: Stream[Double]) =
    neighbors.fzipWith(weights)(_ * _).sum // f zip here acts like a reduce

  // focus is the pointed element
  def smooth(data: NonEmptyList[Double]) = data.toZipper.cobind { z =>
    (z.focus + sumNeighborWeights(z.lefts) + sumNeighborWeights(z.rights)) / 3
  }

  val result = smooth(NonEmptyList[Double](0, 0, 0, 1, 0, 0, 0)).toList
  // List(1 / 24, 1 / 12, 1 / 6, 1 / 3, 1 / 6, 1 / 12, 1 / 24)

}
