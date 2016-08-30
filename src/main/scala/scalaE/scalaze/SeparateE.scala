package scalaE.scalaze

import scala.concurrent.Future

/**
  * @author Emiliano Busiello.
  */
object SeparateE {
  import scalaz._
  import scalaz.Scalaz._

  val p: Future[List[Int \/ Double]] = ???
  val j: Future[(List[Int], List[Double])] = p.map(_.separate[\/, Int, Double])

}
