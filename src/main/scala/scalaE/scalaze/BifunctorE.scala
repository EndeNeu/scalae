package scalaE.scalaze

import scalaz.Bifunctor

object BifunctorE {

  case class MyVali[E, S](tp: (Seq[E], Seq[S]))(implicit bifunctor: Bifunctor[({ type λ[α, β] = (Seq[α], Seq[β]) })#λ]) {
    def bimap[C, D](f: (E) => C, g: (S) => D): (Seq[C], Seq[D]) =
      bifunctor.bimap(tp)(f, g)

    def leftMap[C](f: (E) => C): (Seq[C], Seq[S]) =
      bifunctor.leftMap(tp)(f)

    def rightMap[D](g: (S) => D): (Seq[E], Seq[D]) =
      bifunctor.rightMap(tp)(g)

  }

  val myValBifunctorInstance = new Bifunctor[({ type λ[α, β] = (Seq[α], Seq[β]) })#λ] {
    override def bimap[A, B, C, D](fab: (Seq[A], Seq[B]))(f: (A) => C, g: (B) => D): (Seq[C], Seq[D]) =
      (fab._1.map(f), fab._2.map(g))
  }

  MyVali((Seq.empty[String], Seq.empty[Int]))(myValBifunctorInstance).bimap(a => a, b => b)

}
