package scalaE.definitions

/**
  * A isomorphism is a morphism which admits an inverse
  */
object Isomorphism {

  // splitString is an isomorphism because from the list we can have an inverse (admitted that we know the c: Char)
  def splitString(string: String, c: Char): List[String] =
    string.split(c).toList

  def splitStringInverse(list: List[String], c: Char): String =
    list.mkString(c.toString)

  // reduce though is not an isomorphism, just a morphism:
  List(1,2,3).reduceLeft(_ + _) // now we have no way of decompose the sum and get back the list.

}