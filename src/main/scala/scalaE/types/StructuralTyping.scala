package scalaE.types

class StructuralTyping {

  class someComplex[E, P]

  class someClass[F[_], S[_]]

  /*
  The syntax ({type λ[α]=(R) => α})#λ is known as a type lambda.
  It is a syntactic trick allowing a type alias to be created inline
  and referred to via a projection, so the whole expression can be used
  where a type is required.
   */
  class someOtherClass[E] extends someClass[({type λ[α] = someComplex[E, α]})#λ, ({type λ[α] = someComplex[E, α]})#λ]

  val c: someOtherClass[Int] = new someOtherClass[Int]
}

trait Monad[M[_]] {
  def point[A](a: A): M[A]
  def bind[A, B](m: M[A])(f: A => M[B]): M[B]
}

/*
Now, Either is a type constructor of two arguments, but to implement Monad,
you need to give it a type constructor of one argument. The solution to this is to use a type lambda.
This is an example of currying in the type system - you have curried the type of Either,
such that when you want to create an instance of EitherMonad, you have to specify one of the types;
the other of course is supplied at the time you call point or bind.

({type λ[α] = Either[A, α]}) declare a type λ[α] where α is to be inferred...
#λ and extract that type, so basically this allows to return a curried type where
A is a concrete generic type and α has to be yet inferred.

 */
abstract class EitherMonad[A] extends Monad[({type λ[α] = Either[A, α]})#λ] {
  def point[B](b: B): Either[A, B]
  def bind[B, C](m: Either[A, B])(f: B => Either[A, C]): Either[A, C]
}