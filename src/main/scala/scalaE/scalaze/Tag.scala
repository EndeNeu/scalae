package scalaE.scalaze


import scalaz.{ @@, Tag }


object Tag {

  sealed trait KiloGram

  def KiloGram[A](a: A): A @@ KiloGram = Tag[A, KiloGram](a)

  val mass = KiloGram(20.0)

  2 * Tag.unwrap(mass)
  //res2: Double = 40.0


}
