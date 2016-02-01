package scalaE.scalaze

import scalaz.syntax.SemigroupOps
import scalaz._

/*

LYAHFGG:
A monoid is when you have an associative binary function and a value which acts as an identity with respect to that function.

Monoids a re a simpler form of Monad whcih does't have a bind method,
Ints are monoids because have associativity:
 x + y + z = (x + y) + z = x + (y + z)

 */
object Monoids {

  /*
  Here a method definition is contractive if the type of every implicit parameter type is
  properly contained in the type that is obtained by removing all implicit parameters
  from the method type and converting the rest to a function type.
  For instance, the type of list2ordered is (List[a])(implicit a => Ordered[a]): Ordered[List[a]] .
  This type is contractive, because the type of the implicit parameter, a => Ordered[a],
  is properly contained in the function type of the method without implicit parameters, List[a] => Ordered[List[a]].

  The type of magic is (a)(implicit a => Ordered[a]): Ordered[a] .
  This type is not contractive, because the type of the implicit
  parameter, a => Ordered[a], is the same as the function type of the method without implicit parameters.
   */

  trait SemiGroup[A] {
    def add(x: A, y: A): A
  }

  trait Monoid[A] extends SemiGroup[A]{
    def zero: A
  }

  object Monoids {

    implicit object stringMonoid extends Monoid[String] {
      def add(x: String, y: String): String = x.concat(y)

      def zero: String = ""
    }

    implicit object intMonoid extends Monoid[Int] {
      def add(x: Int, y: Int): Int = x + y

      def zero: Int = 0
    }


    def sum[a](xs: List[a])(implicit m: Monoid[a]): a =
      if (xs.isEmpty) m.zero
      else m.add(xs.head, sum(xs.tail)(m))

    sum(List("a", "bc", "def"))
    sum(List(1, 2, 3))

    new Semigroup[Option[_]] {
      override def append(f1: Option[_], f2: => Option[_]): Option[_] = ???
    }
  }

  new Monoid[(Int, String , Long)] {
    override def zero: (Int, String, Long) = (0, "", 0L)

    override def add(x: (Int, String, Long), y: (Int, String, Long)): (Int, String, Long) =
      (x._1 + y._1, x._2 + y._2, x._3 + y._3)
  }

}