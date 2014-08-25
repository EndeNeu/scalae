package scalaE

class ClassTag {

  def getAs[T: ClassTag](m: Map[String, Any], key: String): T =
    m(key).asInstanceOf[T]

  val map = Map[String, Any]("one" -> 1, "hello"-> "world")
  getAs[String](map, "hello")

}
