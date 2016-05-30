package scalaE

object CategoryHierarchy {

  object SimpleCats {

    trait Bifoldable[F[_, _]] {
      // functors with 2 type parameters which allow bi-folding
      def bifoldLeft[A, B, C](fab: F[A, B], c: C)(f: (C, A) => C, g: (C, B) => C): C
    }

    trait Bifunctor[F[_, _]] {
      // functors with 2 type parameters which allow bi-mapping
      def bimap[A, B, C, D](fab: F[A, B])(f: A => C, g: B => D): F[C, D]
    }

    trait Bitraverse[F[_, _]] extends SimpleCats.Bifoldable[F] with SimpleCats.Bifunctor[F] {
      // Traverse each side of the structure with the given functions
      def bitraverse[G[_]: Functors.Applicative, A, B, C, D](fab: F[A, B])(f: A => G[C], g: B => G[D]): G[F[C, D]]
    }

    trait Cartesian[F[_]] {
      // cartesian product.
      def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
    }

    trait Foldable[F[_]] {
      def foldLeft[A, B](fa: F[A], b: B)(f: (B, A) => B): B
      def foldRight[A, B](fa: F[A], lb: B)(f: (A, B) => B): B
    }

    trait Functor[F[_]] {
      def map[A, B](fa: F[A])(f: A => B): F[B]
    }

    trait Semigroup[F] {
      // a binary operation.
      def append(f1: F, f2: => F): F
    }

    trait Monoid[F] extends SimpleCats.Semigroup[F] {
      // the identity element.
      def zero: F
    }

  }

  object Functors {

    trait Apply[F[_]] extends SimpleCats.Functor[F] with SimpleCats.Cartesian[F] {
      def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
    }

    trait Applicative[F[_]] extends Functors.Apply[F] {
      def pure[A](x: A): F[A]
    }

    trait CoflatMap[F[_]] extends SimpleCats.Functor[F] {
      def coflatMap[A, B](fa: F[A])(f: F[A] => B): F[B]

      def coflatten[A](fa: F[A]): F[F[A]]
    }

    trait FlatMap[F[_]] extends Functors.Apply[F] {
      def flatMap[A, B](fa: F[A])(f: A => B): F[B]
    }

    trait Traverse[F[_]] extends SimpleCats.Functor[F] with SimpleCats.Foldable[F] {
      def traverse[G[_] : Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
    }

  }

  object Monads {

    trait Bimonad[F[_]] extends Monads.Monad[F] with Monads.Comonad[F]

    trait Comonad[F[_]] extends Functors.CoflatMap[F] {
      def extract[A](x: F[A]): A
    }

    trait Monad[F[_]] extends Functors.FlatMap[F] with Functors.Applicative[F]

  }

}
