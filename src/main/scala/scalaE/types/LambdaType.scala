package scalaE.types

/**
  * from http://stackoverflow.com/questions/8736164/what-are-type-lambdas-in-scala-and-what-are-their-benefits
  */
object LambdaType {


  //Consider a simple example of defining a monad for the right projection of Either[A, B].
  // The monad typeclass looks like this:
  trait Monad[M[_]] {
    def point[A](a: A): M[A]
    def bind[A, B](m: M[A])(f: A => M[B]): M[B]
  }


  // Now, Either is a type constructor of two arguments,
  // but to implement Monad, you need to give it a type constructor of one argument.
  // The solution to this is to use a type lambda:

  abstract class EitherMonad[A] extends Monad[({type λ[α] = Either[A, α]})#λ] {
    def point[B](b: B): Either[A, B]
    def bind[B, C](m: Either[A, B])(f: B => Either[A, C]): Either[A, C]
  }

  // This is an example of currying in the type system - you have curried the type of Either,
  // such that when you want to create an instance of EitherMonad,
  // you have to specify one of the types; the other of course is supplied at the time you call point or bind.

}
