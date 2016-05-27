package scalaE.scalaze

import scalaz.Equal

import scalaz._
import Scalaz._

/**
  * All functors are expected to exhibit certain kinds of functor-like properties and behaviors.
  *
  * 1)The first functor law states that if we map the id function over a functor,
  * the functor that we get back should be the same as the original functor.
  * List(1, 2, 3) map {identity} assert_=== List(1, 2, 3)
  *
  * 2) The second law says that composing two functions and then mapping the resulting function over a
  * functor should be the same as first mapping one function over the functor and then mapping the other one.
  * (List(1, 2, 3) map {{(_: Int) * 3} map {(_: Int) + 1}}) assert_=== (List(1, 2, 3) map {(_: Int) * 3} map {(_: Int) + 1})
  *
  * https://github.com/scalaz/scalaz/blob/series/7.1.x/core/src/main/scala/scalaz/Functor.scala#L68-77
  */
object FunctorE {


  import Scalaz._

}
