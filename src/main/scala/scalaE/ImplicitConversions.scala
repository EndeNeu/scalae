package scalaE

class ImplicitConversions {

  implicit class InOperation[T](v: T) {
    def in(s: Set[T]) = {
      s contains v
    }
  }

  2 in Set(1, 2, 3)

}