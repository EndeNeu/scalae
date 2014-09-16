package scalaE.concurrency

import rx.lang.scala.Notification.{OnCompleted, OnError, OnNext}
import rx.lang.scala.{Scheduler, Notification, Observable}
import rx.lang.scala.subjects.{AsyncSubject, BehaviorSubject, PublishSubject, ReplaySubject}
import rx.lang.scala.subscriptions.Subscription

import concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import ExecutionContext.Implicits.global


class Observables {

  val x: Observable[Int] = Observable(3, 2, 1)
  val y: Observable[Observable[Int]] = x.map(z => Observable.interval(z seconds).map(_ => z).take(2))

  // flatten will merge multiple streams in non deterministic order
  val flatted = y.flatten

  // concat will merge the streams in deterministic order, it will matxh the entry sequence.
  val concat = y.concat

  /*
  Hot observables: source is shared from many subscribers. When unsubscribing the observable computation won't stop because there may be other listener on this observable
  Cold observable: each subscriber has is own private source on the observable
   */

  // dummy observable
  val mainSubscr = Observable()

  // there will be no notification if you subscribe to never
  def never[T]: Observable[T] = {
    // except here
    Observable(observer => Subscription{})
  }


  // immediately calls on error with an error and return the empty subscription
  def error(err: Throwable): Observable[Nothing] = Observable(obs => {
    obs.onError(err)
    Subscription {}
  })

  def startWith[T](s: T*): Observable[T] = {
    Observable(obs => {
      for (v <- s) obs.onNext(v)
      mainSubscr.subscribe(obs)
    })
  }

  def filter[T](p: T => Boolean): Observable[T] = {
    Observable(obs => {
      mainSubscr.subscribe(
        // if we get a value and the predicate is true add the value to the observer next
        (t: T) => {
          if (p(t)) obs.onNext(t)
        },
        // if it's a throwable signal an error to the observer
        (e: Throwable) => {
          obs.onError(e)
        },
        // else call complete
        () => {
          obs.onCompleted()
        })
    })
  }

  def map[T, S](f: T => S): Observable[S] = {
    Observable(obs => {
      mainSubscr.subscribe(
        // if we get a value add it to the stream applying f
        (t: T) => {
          obs.onNext(f(t))
        },
        // if it's a throwable signal an error to the observer
        (e: Throwable) => {
          obs.onError(e)
        },
        // else call complete
        () => {
          obs.onCompleted()
        })
    })
  }

  // subjects:
  val c1 = PublishSubject[Int](1)
  val a1 = c1.subscribe(x => println("a1: " + x))
  val b1 = c1.subscribe(x => println("b1: " + x))

  c1.onNext(3) // both a1 and b1 take 3
  a1.unsubscribe()
  c1.onNext(5) // only b1 take 5
  c1.onCompleted()
  // d1 takes the oncomplete signal
  val d1 = c1.subscribe(x => println("d1: " + x))
  // nothing happens because the subject has already called onCompleted.
  c1.onNext(10)


  val c2 = ReplaySubject[Int]()
  val a2 = c2.subscribe(x => println("a1: " + x))
  val b2 = c2.subscribe(x => println("b1: " + x))
  c2.onNext(3) // both a1 and b1 take 3
  a2.unsubscribe()
  c2.onNext(5) // only b1 take 5
  c2.onCompleted()
  // d1 takes here all the buffered elements pushed in the past and then also the onComplete signal
  val d2 = c2.subscribe(x => println("d1: " + x))
  // nothing happens because the subject has already called onCompleted.
  c2.onNext(10)

  val c3 = BehaviorSubject[Int](1)
  val a3 = c3.subscribe(x => println("a1: " + x))
  val b3 = c3.subscribe(x => println("b1: " + x))
  c3.onNext(3) // both a1 and b1 take 3
  a3.unsubscribe()
  c3.onNext(5)
  // only b1 take 5
  // d1 takes the last stored element and the onComplete
  val d3 = c3.subscribe(x => println("d1: " + x))
  c3.onCompleted()
  // nothing happens because the subject has already called onCompleted.
  c3.onNext(10)

  val c4 = AsyncSubject[Int]()
  val a4 = c4.subscribe(x => println("a1: " + x))
  val b4 = c4.subscribe(x => println("b1: " + x))
  c4.onNext(4) // both a1 and b1 take 4
  a4.unsubscribe()
  c4.onNext(5)
  // only b1 take 5
  // d4 takes the last stored element and the onComplete
  val d4 = c4.subscribe(x => println("d1: " + x))
  c4.onCompleted()
  // nothing happens because the subject has already called onCompleted.
  c4.onNext(10)


  // transform a future to observable (promise like)
  def futToObs[T](f: Future[T]): Observable[T] = {
    val subj = AsyncSubject[T]()
    f.onComplete {
      case Failure(t) => subj.onError(t)
      case Success(s) =>
        subj.onNext(s)
        // remember to complete the observer, the future has only one value.
        subj.onCompleted()
    }
    subj
  }

  def toNotification[T](v: T): Observable[Notification[T]] = {
    // materialize will turn a observables of notifications, notifications are similar to try
    // you can match on a notification with OnError, OnNext and OnCompleted.
    val materialized = Observable[T](v).materialize
    materialized.map {
      case OnNext(o) => println("got a next: " + o.toString)
      case OnError(t) => println("got a throwable")
      case _: OnCompleted[_] => println("finished")
    }
    materialized
  }

  // blocking observable
  val obs: Observable[Long] = Observable.interval(1 second).take(5)
  val list: List[Long] = obs.toBlockingObservable.toList
  val sum: Long = obs.sum.toBlockingObservable.single

  // observable on infinite collection:
  def formColl[T](seq: Iterable[T])(implicit scheduler: Scheduler): Observable[T] = {
    Observable(obs => {
      // fold the iterator
      val it = seq.iterator
      // schedule an execution
      scheduler.scheduleRec(self => {
        if(it.hasNext) {
          obs.onNext(it.next())
          // if there was an element in the iterator reschedule yourself, in this way you maz call unsubscribe
          // between one call and the other (it's impossible to call unsubscrive while the thread runs
          // but it is possible to unsubscribe between different calls)
          self
        }
        else obs.onCompleted()
      })
    })
  }

}


