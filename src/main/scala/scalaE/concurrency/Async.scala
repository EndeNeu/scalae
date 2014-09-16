package scalaE.concurrency

import concurrent.{ExecutionContext, Future}
import scala.async.Async.{async, await}
import ExecutionContext.Implicits.global

class Async {

  // @compileTimeOnly("`await` must be enclosed in an `async` block")
  def someMethod[T](x: Future[T]): Future[T] = async {
    // async - await brekas out the T from the future:
    val b: T = await {
      x
    }
    b
  }

}
