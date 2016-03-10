package scalaE.scalaze

/**
  * from http://eed3si9n.com/learning-scalaz/Lens.html
  * and http://docs.typelevel.org/api/scalaz/stable/6.0/doc.sxr/scalaz/example/ExampleLens.scala.html
  */
object LensE {

  import scalaz._
  import Scalaz._


  case class Point(x: Double, y: Double)

  case class Color(r: Byte, g: Byte, b: Byte)

  case class Turtle(
                     position: Point,
                     heading: Double,
                     color: Color)

  val turtlePosition = Lens.lensu[Turtle, Point](
    (a, value) => a.copy(position = value),
    _.position
  )


  case class Employee(name: String, salary: Int)

  val salary: Lens[Employee, Int] = Lens.lensu[Employee, Int]((e, s) => e.copy(e.name, salary = s), _.salary)

  // using the lens method
  val salary2: Lens[Employee, Int] = Lens.lens[Employee, Int](e => Store(x => e.copy(e.name, x), e.salary))
  val name: Lens[Employee, String] = Lens.lensu[Employee, String]((e, n) => e.copy(name = n, e.salary), _.name)

  val giveRaise: Employee => Employee = e => salary mod(_ + 100, e)

  val tom = Employee("Tom", 4000)
  val dick = Employee("Dick", 3000)
  val harry = Employee("Harry", 5000)

  val higherTom = giveRaise(tom) // Employee("Tom", 4100)

  val modBoth = (salary *** name) mod( {
    case (s, n) => (s + 100, n + " Jones")
  }, (harry, tom)) // (Employee("Harry", 5100), Employee("Tom Jones", 4000))

  val modMonadically: IndexedStateT[Id.Id, Employee, Employee, Employee] = for {
    _ <- salary += 100
    n <- name
    _ <- name := n + " Jones"
    e <- init
  } yield e

  val tomJones = modMonadically(tom) // Employee("Tom Jones", 4100)

}
