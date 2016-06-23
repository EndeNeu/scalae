package scalaE.shapelesse

/**
  * from http://stackoverflow.com/questions/37980300/use-shapeless-to-merge-two-instances-of-the-same-case-class
  */
object MergeClasses {

  trait Update[T] {
    def apply(base: T, update: T): T
  }

  import shapeless._

  object Update extends Update0 {
    def apply[A](implicit update: Lazy[Update[A]]): Update[A] = update.value

    implicit def optionUpdate[A]: Update[Option[A]] =
      new Update[Option[A]] {
        def apply(base: Option[A], update: Option[A]): Option[A] = update orElse base
      }

    implicit def listUpdate[A]: Update[List[A]] =
      new Update[List[A]] {
        def apply(base: List[A], update: List[A]): List[A] = base ++ update
      }

    implicit def hnilUpdate: Update[HNil] =
      new Update[HNil] {
        def apply(base: HNil, update: HNil): HNil = HNil
      }

    implicit def hconsUpdate[H, T <: HList](
                                             implicit updateH: Update[H], updateT: Lazy[Update[T]]
                                           ): Update[H :: T] =
      new Update[H :: T] {
        def apply(base: H :: T, update: H :: T): H :: T =
          updateH(base.head, update.head) :: updateT.value(base.tail, update.tail)
      }
  }

  trait Update0 {
    implicit def genericUpdate[A, G <: HList](
                                               implicit gen: Generic.Aux[A, G], updateG: Lazy[Update[G]]
                                             ): Update[A] =
      new Update[A] {
        def apply(base: A, update: A): A =
          gen.from(updateG.value(gen.to(base), gen.to(update)))
      }
  }

  implicit class UpdateOps[A](val base: A) extends AnyVal {
    def update(change: A)(implicit update: Lazy[Update[A]]): A =
      update.value(base, change)
  }

  case class Foo(a: Option[Int], b: List[Int], c: Option[Int])

  val base = Foo(None, Nil, Some(0))
  val update = Foo(Some(3), List(4), None)

  base update update // Foo(Some(3),List(4),Some(0))

  // alternative approach
  import cats.SemigroupK
  import cats.implicits._

  implicit def semigroupKUpdate[F[_], A](implicit F: SemigroupK[F]): Update[F[A]] =
    new Update[F[A]] {
      def apply(base: F[A], update: F[A]): F[A] = F.combineK(update, base)
    }
}
