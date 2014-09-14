package scalaE.types

class PolymorphicSum {

  def sumG[A](term: A => A, a: A, next: A => A, b: A)(implicit n: Numeric[A]): A = {
    if (n.compare(a, b) > 0)
      n.zero
    else
      n.plus(term(a), sumG(term, next(a), next, b))
  }

  println(sumG[Int]({ x => x}, 1, { x => x + 1}, 4))

}
