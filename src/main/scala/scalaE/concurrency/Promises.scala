package scalaE.concurrency

import concurrent.{ExecutionContext, Future, Promise}
import ExecutionContext.Implicits.global


class Promises {


  def race[T](f1: Future[T], f2: Future[T]): Future[T] = {
    val promise = Promise[T]()
    // both futures race to complete the promise.
    f1.onComplete { promise.tryComplete(_) }
    f2.onComplete { promise.tryComplete(_) }
    promise.future
  }

}
