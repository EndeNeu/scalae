package scalaE.shapelesse


/**
  * From http://stackoverflow.com/questions/33585441/constructing-simple-scala-case-classes-from-strings-strictly-without-boiler-pla
  */

import shapeless._

import scala.util.Try

trait Creator[A] { def apply(s: String): Option[A] }

object Creator {
  def create[A](s: String)(implicit c: Creator[A]): Option[A] = c(s)

  def instance[A](parse: String => Option[A]): Creator[A] = new Creator[A] {
    def apply(s: String): Option[A] = parse(s)
  }

  implicit val stringCreate: Creator[String] = instance(Some(_))
  implicit val intCreate: Creator[Int] = instance(s => Try(s.toInt).toOption)
  implicit val doubleCreate: Creator[Double] = instance(s => Try(s.toDouble).toOption)

  implicit val hnilCreator: Creator[HNil] =
    instance(s => if (s.isEmpty) Some(HNil) else None)

  private[this] val NextCell = "^([^,]+)(?:,(.+))?$".r

  implicit def hconsCreate[H: Creator, T <: HList: Creator]: Creator[H :: T] =
    instance {
      case NextCell(cell, rest) => for {
        h <- create[H](cell)
        t <- create[T](Option(rest).getOrElse(""))
      } yield h :: t
      case _ => None
    }

  implicit def caseClassCreate[C, R <: HList](implicit gen: Generic.Aux[C, R], rc: Creator[R] ): Creator[C] =
    instance(s => rc(s).map(gen.from))

}


case class Person(name: String, age: Double)

case class Book(title: String, author: String, year: Int)

case class Country(name: String, population: Int, area: Double)

object CreteClassesFromParams {

  val amy = Creator.create[Person]("Amy,54.2")
  // amy: Option[Person] = Some(Person(Amy,54.2))

  val fred = Creator.create[Person]("Fred,23")
  // fred: Option[Person] = Some(Person(Fred,23.0))

  val hamlet = Creator.create[Book]("Hamlet,Shakespeare,1600")
  // hamlet: Option[Book] = Some(Book(Hamlet,Shakespeare,1600))

  val finland = Creator.create[Country]("Finland,4500000,338424")
  // finland: Option[Country] = Some(Country(Finland,4500000,338424.0))

}
