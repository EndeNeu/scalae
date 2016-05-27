package scalaE.cats

import cats.data.OptionT
import cats.std.list._

object OptionTE {
  val optT: OptionT[List, Int] = OptionT(List(Some(1), None, Some(4)))

  val timeTen = optT.cata(10, _ + 10)
  // List(11, 10, 14)

  val toStringFlatMap: OptionT[List, String] = optT.flatMap(int => OptionT[List, String](List(Some(int.toString))))
  // res0: cats.data.OptionT[List,String] = OptionT(List(Some(1), None, Some(4)))

}
