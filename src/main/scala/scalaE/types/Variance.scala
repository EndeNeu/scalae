package scalaE.types

class Variance {

  /**
   *
   * covariant for the types of immutable fields and method results
   * contravariant for method argument types and upper type parameter bounds
   *
   */
  abstract class GenList[+T] {
    def isEmpty: Boolean

    def head: T

    def tail: GenList[T]

    def prepend[S >: T](x: S): GenList[S] = new Cons(x, this)

  /*
    T is controvariatn position because it appears in the method signature (x: T)
    def prepend(x: (-)T): GenList[(+)T] =
      new Cons(x, this)
   */

  }


  object Empty extends GenList[Nothing] {
    def isEmpty: Boolean = true

    def head: Nothing = throw new Error("Empty.head")

    def tail: GenList[Nothing] = throw new Error("Empty.tail")
  }

  class Cons[+T](x: T, xs: GenList[T]) extends GenList[T] {
    def isEmpty: Boolean = false

    def head: T = x

    def tail: GenList[T] = xs
  }

}
