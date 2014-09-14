package scalaE.types

class PathDependentType {


  case class Board(length: Int, height: Int) {

    case class Coordinate(x: Int, y: Int) {
      require(0 <= x && x < length && 0 <= y && y < height)
    }

    val occupied = scala.collection.mutable.Set[Coordinate]()
  }

  val b1: Board = Board(20, 20)
  val b2: Board = Board(30, 30)
  val c1: b1.Coordinate = b1.Coordinate(15, 15)
  val c2: b2.Coordinate = b2.Coordinate(25, 25)
  b1.occupied += c1
  b2.occupied += c2

  // Next line doesn't compile
  //b1.occupied += c2


  class outer {
    def getInner = new inner()

    class inner {}

  }

  // # syntax is called type selection
  val i: outer#inner = new outer().getInner
  val p: PathDependentType.this.type#outer = new outer()

  /*
  Note that this is conceptually different from a path dependent type p.Inner, where the path p denotes a value (since the path is dependent on the value instanciated), not a type.
  Consequently, the type expression Outer # t is not well-formed if t is an abstract type defined in Outer.


  In fact, path dependent types in Scala can be expanded to type selections. The path dependent type p.t is taken as a shorthand for p.type # t.
  Here, p.type is a singleton type, which represents just the object denoted by p.
   */

  /*
  The type selection p.T, where T is a type member defined in the type of p,
  is syntactic sugar for p.type#T . We say that a type that contains the type p.type
  depends on the path p. The type p.T is a path-dependent type.
   */
}
