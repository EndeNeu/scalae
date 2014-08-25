package scalaE

class Monoids {

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

  abstract class SemiGroup[a] {
    def add(x: a, y: a): a
  }

  abstract class Monoid[a] extends SemiGroup[a] {
    def unit: a
  }

  object Monoids {

    object stringMonoid extends Monoid[String] {
      def add(x: String, y: String): String = x.concat(y)

      def unit: String = ""
    }

    object intMonoid extends Monoid[Int] {
      def add(x: Int, y: Int): Int = x + y

      def unit: Int = 0
    }

  }

  def sum[a](xs: List[a])(implicit m: Monoid[a]): a =
    if (xs.isEmpty) m.unit
    else m.add(xs.head, sum(xs.tail)(m))

  sum(List("a", "bc", "def"))
  sum(List(1, 2, 3))

}
