package scalaE

class TraitForObject {

  trait OnlyForObjects { this: Singleton => }

  // idea bug
  object Foo extends OnlyForObjects

  // doesn't compile
  // class Bar extends OnlyForObjects

}
