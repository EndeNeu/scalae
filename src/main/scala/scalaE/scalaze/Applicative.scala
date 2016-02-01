package scalaE.scalaze

import scalaz.Applicative

object Applicative {
  val ap = new Applicative[List] {
    override def point[A](a: => A): List[A] = List(a)

    override def ap[A, B](fa: => List[A])(f: => List[(A) => B]): List[B] =
      fa.flatMap(l => f.map(ff => ff(l)))
  }

  ap.apply(List(1))(x => x + 1)
  //List[Int] = List(2)

  ap.ap[Int, Int](List(1,2,3))(List(x => x+1, x => 0))
  //List[Int] = List(2, 0, 3, 0, 4, 0)
}