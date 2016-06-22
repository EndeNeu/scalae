package scalaE.types

/**
  * From http://stackoverflow.com/questions/6909053/enforce-type-difference
  */
object TypeInequality {

  trait =!=[A, B]

  implicit def neq[A, B] : A =!= B = null

  // This pair excludes the A =:= B case
  implicit def neqAmbig1[A] : A =!= A = null
  implicit def neqAmbig2[A] : A =!= A = null


  case class Foo[A,B](a : A, b : B)(implicit ev: A =!= B)
  new Foo(1, "1")
  new Foo("foo", Some("foo"))

  // These don't compile
  // new Foo(1, 1)
  // new Foo("foo", "foo")
  // new Foo(Some("foo"), Some("foo"))
}
