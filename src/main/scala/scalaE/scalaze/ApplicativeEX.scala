package scalaE.scalaze

import scala.language.higherKinds
import scalaz.Applicative

/**
  * From https://github.com/scalaz/scalaz/blob/series/7.3.x/example/src/main/scala/scalaz/example/ApplyUsage.scala
  */
object ApplicativeEX {

  import scalaz.Apply
  import scalaz.std.option._
  import scalaz.std.list._
  import scalaz.std.string._
  import scalaz.std.anyVal._
  import scalaz.std.vector._
  import scalaz.std.tuple._
  import scalaz.syntax.equal._
  import scalaz.syntax.std.option._

  val intToString: Int => String = _.toString
  val double: Int => Int = _ * 2
  val addTwo: Int => Int = _ + 2

  // basically directly apply functions to applications

  // map
  assert(Apply[Option].map(1.some)(intToString) === "1".some) // like Option(1).map(intToString)
  assert(Apply[Option].map(1.some)(double) === 2.some)
  assert(Apply[Option].map(none)(double) === none)

  // ap is different, the function applied is a functor function F[A => B] so a functor which holds
  // a function from A to B, in case of option, if that functor holds a none, a non is returned.
  assert(Apply[Option].ap(1.some)(some(intToString)) === "1".some)
  assert(Apply[Option].ap(1.some)(some(double)) === 2.some)
  assert(Apply[Option].ap(none)(some(double)) === none)
  assert(Apply[Option].ap(1.some)(none[Int => Int]) === none[Int])
  assert(Apply[Option].ap(none)(none[Int => Int]) === none[Int])
  assert(Apply[List].ap(List(1,2,3))(List(double, addTwo)) === List(2,4,6,3,4,5))

  // custom applicative for custom class example.
  case class Child[A](param: List[A]) {

    final def map[B](f: A => B): Child[B] =
      Child[B](param.map(f))

    final def flatMap[B](f: A => Child[B]): Child[B] = {
      param.map(f).foldLeft(Child[B](List()))((acc, curr) => {
        Child[B](acc.param ++ curr.param)
      })
    }
  }

  val apB = new Applicative[Child] {
    override def point[A](a: => A): Child[A] = Child(List(a))

    override def ap[A, B](fa: => Child[A])(f: => Child[(A) => B]): Child[B] =
      f.flatMap(func => fa.map(func))
  }

  lazy val applicated = apB.ap(Child(List(1,2,3)))(Child(List(double, addTwo)))
}