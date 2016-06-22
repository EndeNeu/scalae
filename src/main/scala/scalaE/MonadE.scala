package scalaE

import scala.concurrent.Future

/**
  * @author Emiliano Busiello.
  */
trait MonadE[F[_]] {
  def point[A](a: => A): F[A]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

class ListMonadE extends MonadE[List] {
  override def point[A](a: => A): List[A] = List(a)

  override def flatMap[A, B](fa: List[A])(f: (A) => List[B]): List[B] = fa flatMap f
}
