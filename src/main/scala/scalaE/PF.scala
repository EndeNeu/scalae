package scalaE

class PF {
  val a: PartialFunction[String, Unit] = {
    case "hello" => println("bye")
  }

  val b: PartialFunction[String, Unit] = {
    case _ => println("fallback")
  }

  val c = a.orElse(b)

  c("hello")
  //      bye

  c("foo")

  case class NameFinder(getReliableName: String, secondBestChoice: String)

  def chooseName(nameFinder: NameFinder) = {
    Option(nameFinder.getReliableName).filter(_.nonEmpty).getOrElse(nameFinder.secondBestChoice)
  }

}
