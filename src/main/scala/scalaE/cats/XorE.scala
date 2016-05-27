package scalaE.cats

import cats.data.Xor

/**
  * Equivalent of scalaz \/
  */
object XorE {

  val left = Xor.left[Int, String](12)
  val right = Xor.right[Int, String]("123")


  left.map(_ + " 567") // still 12
  right.map(_ + " 567") // 123456


  val matched: Int Xor String = left match {
    case Xor.Left(int) => Xor.left(int + 100)
    case Xor.Right(string) => Xor.right(string + "123")
  }

}