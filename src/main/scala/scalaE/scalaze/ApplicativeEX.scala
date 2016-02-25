package scalaE.scalaze

import scala.collection.immutable.IndexedSeq
import scala.collection.mutable
import scalaz.Applicative

object ApplicativeEx {


  val apOpt = new Applicative[Option] {
    override def point[A](a: => A): Option[A] = Some(a)

    override def ap[A, B](fa: => Option[A])(f: => Option[(A) => B]): Option[B] =
      f.flatMap(func => fa.map(func))
  }

  apOpt.ap(Some("123"))(Some((_: String) + ""))

  val str2 = "LEMELEVEL".tails.toList.init

  def loopSubWord(word: String, acc: Seq[String]): Seq[String] = {
    def inLoop(string: String): Seq[String] = {

      var w: String = string.head.toString
      var l = mutable.Seq.empty[String]

      string.tail.foreach(c => {
        w = w + c
        if (w == w.reverse) l +: w
      })
      l
    }

    if (word.isEmpty) acc
    else loopSubWord(word.tail, acc ++ inLoop(word))
  }

  str2.flatMap(subword => {
    loopSubWord(subword, List())
  }).distinct


  val str = "LEMELEVEL".toList
  for {
    i <- 1 to str.length
    pairs <- str.sliding(i)
    if pairs == pairs.reverse
  } yield pairs



  def wordCombinations(word: String) = {
    def loop(prefix: String, w: String, acc: List[String]): List[String] = w.isEmpty match {
      case false =>
        val combination = prefix + w.head
        if(isPaly(combination)) loop(combination, w.tail, acc :+ combination)
        else loop(combination, w.tail, acc)
      case true =>
        acc
    }

    def isPaly(word: String): Boolean = {
      println(word)
      word == word.reverse
    }

    loop("", word, List())
  }


  val w = "LEMEL".toList

  for {
    i <- 1 to w.length
    slided <- w.sliding(i)
    if slided == slided.reverse
  } yield slided

  val paliWord = "LEMELEVEL".tails.toList

  val palies: List[String] = paliWord.flatMap(word => wordCombinations(word)).distinct

  def removeChar(word: String, c: Char): String = {
    word.filter(_ == c)
  }

  // How to find first non repeated character of a given String?
  def findNonRepeated(word: String): Option[Char] = {
    println(word)
    if (word.isEmpty) None
    else {
      val head = word.head
      val tail = word.tail
      tail.find(_ == head) match {
        case Some(char) => findNonRepeated(tail.filter(_ != head))
        case None => Some(head)
      }
    }
  }

}