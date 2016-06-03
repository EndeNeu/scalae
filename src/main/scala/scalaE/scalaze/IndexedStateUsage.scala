package scalaE.scalaze

import scala.collection.parallel.Task
import scala.concurrent.{Await, Future}
import scalaz.{Functor, IMap, Monad, Ordering, StateT}
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz.std.scalaFuture._
import scalaz.Order._
import scalaz.std.math.OrderingInstances

/**
  * From https://speakerdeck.com/vmarquez/index-your-state-for-safer-functional-apis
  */
object IndexedStateUsage {

  implicit val ord = new scalaz.Order[Int] {
    override def order(x: Int, y: Int): Ordering = if (x > y) Ordering.GT else Ordering.LT
  }

  case class SState[A, B](f: (A => (A, B))) {
    def flatMap[C](fc: B => SState[A, C]): SState[A, C] =
      SState((a: A) => f(a) match {
        case (a, b) => fc(b).f(a)
      })

    def map[C](fc: B => (A, C)): SState[A, C] =
      SState((a: A) => f(a) match {
        case (a, b) => fc(b)
      })
  }

  def countState =
    SState[Int, String]((i: Int) => (i + 1, i.toString))

  def count: SState[Int, String] = for {
    a <- countState
    b <- countState
  } yield (a.toInt + b.toInt, (a.toInt + b.toInt).toString)

  case class SStateT[F[_], A, B](f: (A => F[(A, B)])) {
    def flatMap[C](fc: B => SStateT[F, A, C])(implicit M: Monad[F]) =
      SStateT[F, A, C](a => {
        /*f(a).flatMap(aa => aa match { case (aaa, b) => fc(b).f(aaa) })*/
        M.bind(f(a)) { case (aaa, b) => fc(b).f(aaa) }
      })

    def map[C](fc: B => (A, C))(implicit F: Functor[F]) =
      SStateT[F, A, C](a => {
        F.map(f(a)) {
          case (aaa, b) => fc(b)
        }
      })
  }

  def countStateT =
    SStateT[Future, Int, String]((i: Int) => Future((i + 1, i.toString)))

  def countT =
    for {
      a <- countStateT
      b <- countStateT
    } yield (a.toInt + b.toInt, (a.toInt + b.toInt).toString)

  import scala.concurrent.duration._

  Await.result(IndexedStateUsage.countT.f(1), 1 second)

  case class Item(item: String)

  case class CachedState(map: IMap[Int, Item])

  def lockItem(i: Int): StateT[Future, CachedState, Unit] = ???

  def updateItemName(id: Int, name: String): StateT[Future, CachedState, Item] = ???

  def commitChange(): StateT[Future, CachedState, Item] = ???

  def viewItem(i: Int): StateT[Future, CachedState, Item] =
    StateT((cis: CachedState) => Future {
      cis.map.lookup(i) match {
        case Some(item) => (cis, item)
        case None =>
          // threading over the item here.
          val item: Item = ??? /* viewItemUnsafe */
        val nmap = cis.map + (i -> item)
          (CachedState(nmap), item)
      }
    })

  // this compiles!
  for {
    a <- viewItem(4)
    _ <- lockItem(4)
    _ <- updateItemName(4, "123")
  } yield a

  case class ISStateT[F[_], A, B, C](f: (A => F[(B, C)])) {
    def flatMap[D, E](fc: C => ISStateT[F, B, D, E])(implicit M: Monad[F]): ISStateT[F, A, D, E] =
      ISStateT(a => {
        M.bind(f(a)) { case (b, c) => fc(c).f(b) }
      })

    def map[D, E](fc: C => (D, E))(implicit F: Functor[F]): ISStateT[F, A, D, E] =
      ISStateT(a => {
        F.map(f(a)) { case (b, c) => fc(c) }
      })
  }

}